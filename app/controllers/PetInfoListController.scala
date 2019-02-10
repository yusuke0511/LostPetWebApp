package controllers

import java.nio.file.{ Files, Path, Paths }

import forms.PetInfoRegistorForm
import javax.inject._
import models.pet.{ Gender, PetKind, PetSearchInfo }
import org.webjars.play.WebJarsUtil
import play.api.Play._
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{ I18nSupport, Lang, Langs }
import play.api.libs.Collections
import play.api.mvc._

/**
 * ペット迷子情報一覧画面
 * @param petSearchInfo 迷子ペット情報一覧
 */
@Singleton
class PetInfoListController @Inject() (petSearchInfo: PetSearchInfo, cc: ControllerComponents)(implicit webJarsUtil: WebJarsUtil, assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  import play.api.data.validation.Constraints._

  /**
   * 初期画面表示
   */
  def init = Action { implicit request =>
    val list: List[Map[String, Any]] = petSearchInfo.getPetInfoList
    Ok(views.html.petInfoList(true, list))
  }

  def test(imagePath: String): String = {
    val path: Path = Paths.get("tmp/picture/" + imagePath)
    if (Files.exists(path)) {
      return path.toString
    }
    return ""
  }
}
