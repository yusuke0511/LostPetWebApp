package controllers

import com.mohiva.play.silhouette._
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import javax.inject._
import models.pet.{ Gender, PetKind, PetSearchInfo }
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import forms.PetInfoRegistorForm
import models.User
import org.webjars.play.WebJarsUtil
import play.api.i18n.{ I18nSupport, Messages }
import play.api.libs.json.Json
import utils.auth.{ DefaultEnv, WithProvider }

import scala.concurrent.Future
import scala.util.parsing.json.JSONObject

/**
 * ペット迷子情報登録画面
 * @param gender
 * @param petKind
 * @param petSearchInfo
 * @param cc
 * @param silhouette
 * @param assetsFinder
 * @param webJarsUtil
 */
@Singleton
class PetInfoRegistorController @Inject() (
  gender: Gender, petKind: PetKind, petSearchInfo: PetSearchInfo,
  cc: ControllerComponents, silhouette: Silhouette[DefaultEnv])(
  implicit
  assetsFinder: AssetsFinder, webJarsUtil: WebJarsUtil)
  extends AbstractController(cc) with I18nSupport {

  val formVal = Form(mapping(
    "name" -> nonEmptyText,
    "gender_type" -> number,
    "pet_kind_type" -> number,
    "feature" -> text,
    "pref" -> number,
    "lostPlace" -> text,
    "lat" -> text,
    "lng" -> text)(PetInfoRegistorForm.apply)(PetInfoRegistorForm.unapply))

  /**
   * 初期画面表示
   */
  def init = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
      Ok(views.html.petInfoRegstor(
        true,
        gender.genderList,
        petKind.petKindList,
        formVal,
        request.identity,
        routes.PetInfoRegistorController.regist))
  }

  def edit(id: Long) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
      val result: List[Map[String, Any]] = petSearchInfo.getPetInfoList(id)
      val petRegistData = result.apply(0)

      val p: PetInfoRegistorForm = PetInfoRegistorForm(
        petRegistData.get("name").get.toString,
        petRegistData.get("gender").get.toString.toInt,
        petRegistData.get("kind").get.toString.toInt,
        petRegistData.get("feature").get.toString,
        petRegistData.get("pref").get.toString.toInt,
        petRegistData.get("place").get.toString,
        petRegistData.get("lat").get.toString,
        petRegistData.get("lng").get.toString)

      Ok(views.html.petInfoRegstor(
        true,
        gender.genderList,
        petKind.petKindList,
        formVal.fill(p),
        request.identity,
        routes.PetInfoRegistorController.update))
  }

  /**
   * 登録ボタン押下処理
   * @return
   */
  def regist = silhouette.SecuredAction(parse.multipartFormData) {
    implicit request =>
      println("start")
      println(formVal)

      formVal.bindFromRequest.fold(
        errorForm => {
          Ok(views.html.petInfoRegstor(
            false,
            gender.genderList,
            petKind.petKindList,
            errorForm,
            request.identity,
            routes.PetInfoRegistorController.regist))
        },
        requestForm => {
          println("push button!!!!")

          request.body.file("petImg").map { picture =>
            import java.io.File
            import org.joda.time.{ DateTime, DateTimeZone }

            val juDate = new java.util.Date()
            val dt = new DateTime(juDate)

            val filename = dt.getMillis.toString + "_" + picture.filename
            val contentType = picture.contentType

            picture.ref.moveTo(new File(s"public/tmp/picture/$filename"))
            //            println("resize -> " + picture.filename)
            //            Process("convert -thumbnail 220x220 -quality 70 " + s"public/tmp/picture/$filename") run

            println("File Upload OK : " + filename)
            Ok("File uploaded")

            var res = petSearchInfo.insert(requestForm, request.identity.userID.toString, filename)
            Redirect(routes.PetInfoRegistorController.complete)

          }.getOrElse {
            Redirect(routes.PetInfoRegistorController.complete).flashing(
              "error" -> "Missing file")
          }
        })
  }

  def update = TODO

  def complete = Action {
    Ok(views.html.petInfoRegistorComplete(""))
  }
}
