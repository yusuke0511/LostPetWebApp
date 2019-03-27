package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.PetInfoRegistorForm
import javax.inject._
import models.pet.PetSearchInfo
import org.webjars.play.WebJarsUtil
import play.api.Logger
import play.api.cache.SyncCacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import services.TwitterService
import util.CacheUtil
import utils.auth.{ DefaultEnv, WithProvider }

/**
 * ペット迷子情報画面
 */
@Singleton
class PetInfoPreviewController @Inject() (
  petSearchInfo: PetSearchInfo,
  cc: ControllerComponents,
  silhouette: Silhouette[DefaultEnv],
  cache: SyncCacheApi)(
  implicit
  webJarsUtil: WebJarsUtil, assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  val log = Logger(this.getClass)

  /**
   * 登録ボタン押下処理
   * @return
   */
  def regist = silhouette.SecuredAction(parse.multipartFormData) {
    implicit request =>
      val uuid = request.session.get("userId").get;
      val imagePath = CacheUtil.getImagePath(cache, uuid)

      val pathList = imagePath match {
        case Some(x) => x.path
        case None => {
          log.error("イメージが取得できなかった -> id:" + uuid)
          List("")
        }
      }

      val form = CacheUtil.getPetRegistorForm(cache, uuid)
      val petInfo = form match {
        case Some(x) => {

          // DB insert
          petSearchInfo.insert(x, uuid, pathList)

          val myDataList = petSearchInfo.getOwnPetInfoList(uuid)
          val myData = myDataList(0)

          // Message tweet for Twitter
          val tweetMsg = "【拡散希望】迷子ペットを探してます！\n" + "名前：" + x.name + "\n" +
            "http://localhost:9000/petInfoDisplay/" + myData.get("id").get

          import java.nio.file.FileSystems
          val fs = FileSystems.getDefault
          val path = fs.getPath("public/tmp/picture/" + pathList(0))
          val file = path.toFile

          TwitterService.postImageTweet(tweetMsg, file)
        }
        case None => log.error("When post tweet, PetRegistoInfomation is nothing")
      }

      Redirect(routes.PetInfoRegistorController.complete)
  }
}
