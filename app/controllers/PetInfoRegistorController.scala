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
import play.api.cache.SyncCacheApi
import play.api.i18n.{ I18nSupport, Messages }
import play.api.libs.json.Json
import utils.auth.{ DefaultEnv, WithProvider }
import util.CacheUtil

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
  cc: ControllerComponents, silhouette: Silhouette[DefaultEnv], cache: SyncCacheApi)(
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
      cache.remove("PetInfoRegistorForm")
      Ok(views.html.petInfoRegstor(
        true,
        gender.genderList,
        petKind.petKindList,
        formVal,
        request.identity,
        routes.PetInfoRegistorController.comfirm))
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

  def comfirm = silhouette.SecuredAction(parse.multipartFormData) {
    implicit request =>
      formVal.bindFromRequest.fold(
        errorForm => {
          Ok(views.html.petInfoRegstor(
            false,
            gender.genderList,
            petKind.petKindList,
            errorForm,
            request.identity,
            routes.PetInfoRegistorController.comfirm))
        },
        requestForm => {
          CacheUtil.set(cache, "PetInfoRegistorForm", requestForm)
          Ok(views.html.petInfoPreview(requestForm, request.identity, routes.PetInfoPreviewController.regist))
        })
  }

  def update = TODO

  def complete = Action {
    Ok(views.html.petInfoRegistorComplete(""))
  }
}
