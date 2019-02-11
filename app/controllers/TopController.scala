package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.i18n.I18nSupport
import play.api.mvc._
import utils.auth.{ DefaultEnv, WithProvider }

@Singleton
class TopController @Inject() (cc: ControllerComponents, silhouette: Silhouette[DefaultEnv])(
  implicit
  assetsFinder: AssetsFinder, webJarsUtil: WebJarsUtil)
  extends AbstractController(cc) with I18nSupport {

  def index = Action { implicit request =>
    Ok(views.html.top("", user = null))
  }

  def init = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
      println(request.identity)
      Ok(views.html.top("", request.identity))
  }
}
