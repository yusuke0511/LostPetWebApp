package models.pet

import javax.inject._
import play.api.mvc._
import play.api.db._
import anorm._
import anorm.SqlParser._

@Singleton
class PetKind @Inject() (db: Database) {
  val parser = int("pet_type") ~ str("pet_kind_name")
  val mapper = parser.map {
    case pet_type ~ pet_kind_name => Map("pet_type" -> pet_type, "pet_kind_name" -> pet_kind_name)
  }

  def petKindList: List[Map[String, Any]] = {
    db.withConnection { implicit c =>
      SQL("SELECT * FROM pet_kind ORDER BY pet_type").as(mapper.*)
    }
  }
}
