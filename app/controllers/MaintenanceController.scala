package controllers

import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.mvc._

@Singleton
class MaintenanceController @Inject() (cc: ControllerComponents)(implicit webJarsUtil: WebJarsUtil, assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def init = Action {
    Ok(views.html.maintenance(""))
  }
}
