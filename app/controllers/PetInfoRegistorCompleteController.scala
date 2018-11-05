package controllers

import javax.inject._
import play.api.mvc._


@Singleton
class PetInfoRegistorCompleteController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def init = Action {
    Ok(views.html.petInfoRegistorComplete(""))
  }
}
