package utils.auth

import java.util

import javax.inject.Inject
import com.mohiva.play.silhouette.api.actions.SecuredErrorHandler
import play.api.i18n.{ I18nSupport, Messages, MessagesApi }
import play.api.mvc.RequestHeader
import play.api.mvc.Results._

import scala.concurrent.Future

/**
 * Custom secured error handler.
 *
 * @param messagesApi The Play messages API.
 */
class CustomSecuredErrorHandler @Inject() (val messagesApi: MessagesApi) extends SecuredErrorHandler with I18nSupport {

  /**
   * Called when a user is not authenticated.
   *
   * As defined by RFC 2616, the status code of the response should be 401 Unauthorized.
   *
   * @param request The request header.
   * @return The result to send to the client.
   */
  override def onNotAuthenticated(implicit request: RequestHeader) = {
    val target: Array[String] = request.target.path.split("/")

    if (target.size > 0) {
      target(1) match {
        case "petInfoRegistor" => Future.successful(Redirect(controllers.auth.routes.SignInController.view()))
        case "auth" => Future.successful(Redirect(controllers.auth.routes.SignInController.view()))
        case "petInfoList" => Future.successful(Redirect(controllers.routes.PetInfoListController.notAuth()))
        case "petInfoDisplay" => Future.successful(Redirect(controllers.routes.PetInfoDisplayController.noAuth(target(2).toInt)))
        case _ => Future.successful(Redirect(controllers.routes.TopController.index()))
      }
    } else {
      Future.successful(Redirect(controllers.routes.TopController.index()))
    }
  }

  /**
   * Called when a user is authenticated but not authorized.
   *
   * As defined by RFC 2616, the status code of the response should be 403 Forbidden.
   *
   * @param request The request header.
   * @return The result to send to the client.
   */
  override def onNotAuthorized(implicit request: RequestHeader) = {
    Future.successful(Redirect(controllers.auth.routes.SignInController.view()).flashing("error" -> Messages("access.denied")))
  }
}
