package controllers

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.mvc._


@Singleton
class LoginController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.login("Your new application is ready."))
  }
}
