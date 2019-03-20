package controllers

import java.nio.file.Paths

import com.mohiva.play.silhouette.api.Silhouette
import play.api.libs.concurrent.CustomExecutionContext

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject._
import models.ImagePath
import org.webjars.play.WebJarsUtil
import play.api.Logger
import play.api.cache.SyncCacheApi
import play.api.i18n.I18nSupport
import play.api.mvc._
import util.CacheUtil
import utils.auth.DefaultEnv

import scala.concurrent.Future
import scala.reflect.io.File
import scala.sys.process.Process

@Singleton
class PetInfoImageRegistorController @Inject() (
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv], cache: SyncCacheApi)(
  implicit
  assetsFinder: AssetsFinder, webJarsUtil: WebJarsUtil)
  extends AbstractController(cc) with I18nSupport {

  val logger = Logger("application")

  def comfirm = silhouette.SecuredAction(parse.multipartFormData) {
    implicit request =>
      val uuid = request.session.get("userId").get;
      Ok(views.html.petInfoPreview(
        CacheUtil.getPetRegistorForm(cache, uuid).get,
        CacheUtil.getImagePath(cache, uuid).orElse(None),
        request.identity,
        routes.PetInfoPreviewController.regist))
  }

  def upload = Action(parse.multipartFormData) {
    implicit request =>
      val uuid = request.session.get("userId").get;

      request.body.file("fileInput").map { picture =>

        if (picture.filename != "") {

          // アップロードファイルに時間+ランダム文字列でファイル名を付ける
          val filename = getImageFileName + "." + picture.filename.split('.').last
          val contentType = picture.contentType

          picture.ref.moveTo(Paths.get(s"upload/$filename").toFile, replace = false)

          /* ImageMagic
           * -auto-orient = Auto rotation
           * -thunbnail = Resize And delete Exif
           * -quality
           */
          val command = "convert -auto-orient -thumbnail 220x220 -quality 70 -strip ";
          Process(command + s"upload/$filename" + " " + s"public/tmp/picture/$filename").run
          logger.debug("File Upload OK : " + filename)

          var imagePath = CacheUtil.getImagePath(cache, uuid)
          imagePath match {
            case Some(x) => {
              CacheUtil.set(cache, uuid + CacheUtil.KEY_ImagePath, ImagePath.apply(filename :: x.path))
            }
            case None => {
              imagePath = Option(ImagePath.apply(List(filename)))
              CacheUtil.set(cache, uuid + CacheUtil.KEY_ImagePath, imagePath.get)
            }
          }

        }
        // 画面に画像を表示するため画像アップロード完了まで処理を止める（暫定対応）
        Thread.sleep(2000)
        Redirect(routes.PetInfoImageRegistorController.imagePreview())

      }.getOrElse {
        Redirect(routes.PetInfoImageRegistorController.comfirm())
      }
  }

  def getImageFileName: String = {
    import org.joda.time.DateTime

    val juDate = new java.util.Date()
    val dt = new DateTime(juDate)

    dt.toString("yyyyMMdd") +
      dt.getHourOfDay + dt.getMinuteOfDay +
      dt.getSecondOfDay + "_" + dt.getMillis.toString + "_" +
      scala.util.Random.alphanumeric.take(10).mkString
  }

  def imagePreview = Action {
    implicit request =>
      val uuid = request.session.get("userId").get

      Ok(views.html.petInfoImageRegstor(
        true,
        CacheUtil.getLoginInfo(cache, uuid).get,
        CacheUtil.getImagePath(cache, uuid),
        routes.PetInfoImageRegistorController.comfirm))
  }

  def deleteImage = Action {
    implicit request =>
      val id = request.body.asFormUrlEncoded.get("imageId")(0).toInt
      val uuid = request.session.get("userId").get
      val imagePath = CacheUtil.getImagePath(cache, uuid)
      val filePath = imagePath.getOrElse(new ImagePath(List("")))

      if (filePath.path.size > id) {
        val imageFile = File("public/tmp/picture/" + filePath.path(id))
        imageFile.delete

        // 削除対象のパスを抜いたリストの再作成
        val path = imagePath.get.path.filter(!_.equals(imagePath.get.path(id)))
        CacheUtil.set(cache, uuid + CacheUtil.KEY_ImagePath, ImagePath.apply(path.map(_.toString)))
      }

      Ok(views.html.petInfoImageRegstor(
        true,
        CacheUtil.getLoginInfo(cache, uuid).get,
        CacheUtil.getImagePath(cache, uuid),
        routes.PetInfoImageRegistorController.comfirm))
  }
}
