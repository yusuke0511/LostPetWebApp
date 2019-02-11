package controllers

import java.nio.file.{ Files, Path, Paths }

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
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
import utils.auth.{ DefaultEnv, WithProvider }

/**
 * ペット迷子情報一覧画面
 * @param petSearchInfo 迷子ペット情報一覧
 */
@Singleton
class PetInfoListController @Inject() (petSearchInfo: PetSearchInfo, cc: ControllerComponents, silhouette: Silhouette[DefaultEnv])(
  implicit
  webJarsUtil: WebJarsUtil, assetsFinder: AssetsFinder)
  extends AbstractController(cc) with I18nSupport {

  import play.api.data.validation.Constraints._

  val list: List[Map[String, Any]] = petSearchInfo.getPetInfoList
  /**
   * 初期画面表示
   */
  def notAuth = Action { implicit request =>
    Ok(views.html.petInfoList(list, user = null))
  }

  def init = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)) {
    implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
      println(request.identity)
      Ok(views.html.petInfoList(list, request.identity))
  }

  def test(imagePath: String): String = {
    val path: Path = Paths.get("tmp/picture/" + imagePath)
    if (Files.exists(path)) {
      return path.toString
    }
    return ""
  }
}
