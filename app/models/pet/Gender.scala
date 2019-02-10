package models.pet

import javax.inject._
import play.api.mvc._
import play.api.db._
import anorm._
import anorm.SqlParser._

@Singleton
class Gender @Inject() (db: Database) {
  val parser = int("gender_type") ~ str("gender_name")
  val mapper = parser.map {
    case gender_type ~ gender_name => Map("gender" -> gender_type, "gender_name" -> gender_name)
  }

  def genderList: List[Map[String, Any]] = {
    db.withConnection { implicit c =>
      SQL("SELECT * FROM gender ORDER BY gender_type").as(mapper.*)
    }
  }
}
