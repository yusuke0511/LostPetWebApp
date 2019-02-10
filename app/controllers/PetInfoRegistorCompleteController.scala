package controllers

import javax.inject._
import org.webjars.play.WebJarsUtil
import play.api.mvc._

@Singleton
class PetInfoRegistorCompleteController @Inject() (cc: ControllerComponents)(implicit assetsFinder: AssetsFinder, webJarsUtil: WebJarsUtil)
  extends AbstractController(cc) {

  def init = Action {
    Ok(views.html.petInfoRegistorComplete(""))
  }
}
