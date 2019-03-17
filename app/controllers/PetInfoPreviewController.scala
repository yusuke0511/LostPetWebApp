package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.PetInfoRegistorForm
import javax.inject._
import models.pet.PetSearchInfo
import org.webjars.play.WebJarsUtil
import play.api.cache.SyncCacheApi
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
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

  /**
   * 登録ボタン押下処理
   * @return
   */
  def regist = silhouette.SecuredAction(parse.multipartFormData) {
    implicit request =>
      var res = petSearchInfo.insert(CacheUtil.getPetRegistorForm(cache, request.identity.userID.toString).get, request.identity.userID.toString, "")
      Redirect(routes.PetInfoRegistorController.complete)
  }
}
