package controllers

import javax.inject._
import models.pet.{Gender, PetKind, PetSearchInfo}
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import forms.PetInfoRegistorForm
import play.api.Play._
import play.api.i18n.I18nSupport

/**
  * ペット迷子情報登録画面
  * @param gender
  * @param petKind
  */
@Singleton
class PetInfoRegistorController @Inject()(gender:Gender, petKind:PetKind, petSearchInfo:PetSearchInfo,cc: ControllerComponents) (implicit assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  val formVal = Form(mapping(
    "name" -> text,
    "gender_type" -> number,
    "pet_kind_type" -> number,
    "feature" -> text,
    "pref" -> number,
    "lostPlace" -> text
    )
    (PetInfoRegistorForm.apply)
    (PetInfoRegistorForm.unapply)
  )


  /**
    * 初期画面表示
    */
  def init = Action {implicit request =>
    Ok(views.html.petInfoRegstor(
      true,
      gender.genderList,
      petKind.petKindList,
      formVal)
    )
  }

  /**
    * 登録ボタン押下処理
    * @return
    */
  def regist= Action(parse.multipartFormData) {implicit  request =>
    println("start")
    println(formVal)
    formVal.bindFromRequest.fold(
        errorForm => {
          Ok(views.html.petInfoRegstor(
            false,
            gender.genderList,
            petKind.petKindList,
            errorForm)
          )
        },
        requestForm => {
          println("push button!!!!")

          request.body.file("petImg").map { picture =>
            import java.io.File
            import org.joda.time.{DateTime, DateTimeZone}

            val juDate = new java.util.Date()
            val dt = new DateTime(juDate)


            val filename = dt.getMillis.toString + "_" +  picture.filename
            val contentType = picture.contentType
            picture.ref.moveTo(new File(s"public/tmp/picture/$filename"))

            println("File Upload OK : " + filename)
            Ok("File uploaded")

            var res = petSearchInfo.insert(requestForm, filename)
            Redirect(routes.PetInfoRegistorController.complete)

          }.getOrElse {
            Redirect(routes.PetInfoRegistorController.complete).flashing(
              "error" -> "Missing file")
          }
       }
    )
  }

  def complete = Action{
    Ok(views.html.petInfoRegistorComplete(""))
  }
}
