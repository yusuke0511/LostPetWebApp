package controllers

import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.mvc._

@Singleton
class TopController @Inject() (cc: ControllerComponents)(implicit assetsFinder: AssetsFinder, webJarsUtil: WebJarsUtil)
  extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.top(""))
  }
}
