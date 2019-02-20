package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.PetInfoRegistorForm
import javax.inject._
import models.pet.{ Gender, PetKind, PetSearchInfo }
import org.webjars.play.WebJarsUtil
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.auth.{ DefaultEnv, WithProvider }

/**
 * ペット迷子情報画面
 */
@Singleton
class PetInfoDisplayController @Inject() (petSearchInfo: PetSearchInfo, cc: ControllerComponents, silhouette: Silhouette[DefaultEnv])(
  implicit
  webJarsUtil: WebJarsUtil, assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  /**
   * 初期画面表示
   */
  def init(id: Long) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
      val list: List[Map[String, Any]] = petSearchInfo.getPetInfoList(id)
      if (list.size != 1) {
        Ok(views.html.petInfoDisplay(true, list, request.identity))
      } else {
        Ok(views.html.petInfoDisplay(false, list, request.identity))
      }
  }

  def noAuth(id: Long) = Action { implicit request =>
    val list: List[Map[String, Any]] = petSearchInfo.getPetInfoList(id)
    if (list.size != 1) {
      Ok(views.html.petInfoDisplay(true, list, user = null))
    } else {
      Ok(views.html.petInfoDisplay(false, list, user = null))
    }
  }
}
