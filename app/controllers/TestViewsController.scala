package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class TestViewsController @Inject()(cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) {

  def index = Action {
    Ok(views.html.test_view("ペット迷子捜索版"))
  }
}
