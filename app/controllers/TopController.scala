package controllers

import javax.inject._
import play.api.mvc._


@Singleton
class TopController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.top(""))
  }


}
