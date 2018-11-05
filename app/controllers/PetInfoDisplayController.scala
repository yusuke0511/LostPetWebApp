package controllers

import forms.PetInfoRegistorForm
import javax.inject._
import models.pet.{Gender, PetKind, PetSearchInfo}
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.I18nSupport
import play.api.mvc._

/**
  * ペット迷子情報画面
  */
@Singleton
class PetInfoDisplayController @Inject()(petSearchInfo:PetSearchInfo, cc: ControllerComponents)(implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  /**
    * 初期画面表示
    */
  def init(id:Int) = Action {implicit request =>
    val list:List[Map[String,Any]] =  petSearchInfo.getPetInfoList(id)
    if (list.size != 1) {
      Ok(views.html.petInfoDisplay(true, list))
    } else {
      Ok(views.html.petInfoDisplay(false, list))
    }
  }
}
