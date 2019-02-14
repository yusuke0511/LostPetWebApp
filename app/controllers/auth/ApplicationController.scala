package controllers.auth

import javax.inject.Inject
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{ LogoutEvent, Silhouette }
import controllers.AssetsFinder
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc.{ AbstractController, AnyContent, ControllerComponents }
import utils.auth.DefaultEnv
import models.pet.PetSearchInfo

import scala.concurrent.Future

/**
 * The basic application controller.
 *
 * @param components  The Play controller components.
 * @param silhouette  The Silhouette stack.
 * @param webJarsUtil The webjar util.
 * @param assets      The Play assets finder.
 */
class ApplicationController @Inject() (
  petSearchInfo: PetSearchInfo,
  components: ControllerComponents,
  silhouette: Silhouette[DefaultEnv])(
  implicit
  webJarsUtil: WebJarsUtil,
  assets: AssetsFinder) extends AbstractController(components) with I18nSupport {

  /**
   * Handles the index action.
   *
   * @return The result to display.
   */
  def index = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(
      Ok(
        views.html.auth.home(
          petSearchInfo.getOwnPetInfoList(request.identity.userID.toString), request.identity)))
  }

  def delete(id: String) = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>

    val user_id = request.identity.userID.toString
    val res = petSearchInfo.deletePetInfoList(user_id, id.toInt)
    println("削除件数:" + res + " :user_id -> " + user_id + " :id -> " + id)

    Future.successful(
      Ok(
        views.html.auth.home(
          petSearchInfo.getOwnPetInfoList(request.identity.userID.toString), request.identity)))
  }

  /**
   * Handles the Sign Out action.
   *
   * @return The result to display.
   */
  def signOut = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(controllers.auth.routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }
}
