package models.pet

import forms.PetInfoRegistorForm
import javax.inject._
import play.api.mvc._
import play.api.db._
import anorm._
import anorm.SqlParser._

@Singleton
class PetSearchInfo @Inject() (db: Database) {

  val parser = long("id") ~ str("name") ~ int("gender") ~ int("kind") ~ str("feature") ~ int("pref") ~ str("place") ~ str("image_path") ~ date("create_time") ~ date("update_time")
  val mapper = parser.map {
    case id ~ name ~ gender ~ kind ~ feature ~ pref ~ place ~ image_path ~ create_time ~ update_time => Map("id" -> id, "name" -> name, "gender" -> gender, "kind" -> kind,
      "feature" -> feature, "pref" -> pref, "place" -> place, "image_path" -> image_path,
      "create_time" -> create_time, "update_time" -> update_time)
  }

  def getPetInfoList: List[Map[String, Any]] = {
    db.withConnection { implicit c =>
      SQL("SELECT * FROM pet_search_info ORDER BY id desc")
        .as(mapper.*)
    }
  }

  def getPetInfoList(id: Long): List[Map[String, Any]] = {
    db.withConnection { implicit c =>
      SQL("SELECT * FROM pet_search_info WHERE id = {id} ORDER BY id desc").on('id -> id)
        .as(mapper.*)
    }
  }

  def getOwnPetInfoList(user_id: String): List[Map[String, Any]] = {
    db.withConnection { implicit c =>
      SQL("SELECT * FROM pet_search_info WHERE user_id = {user_id} ORDER BY id desc").on('user_id -> user_id)
        .as(mapper.*)
    }
  }

  def deletePetInfoList(user_id: String, id: Long) = {
    db.withConnection { implicit c =>
      SQL("DELETE FROM pet_search_info WHERE user_id = {user_id} and id = {id}").on(
        'user_id -> user_id,
        'id -> id).executeUpdate()
    }
  }

  def insert(r: PetInfoRegistorForm, userId: String, image_path: String): Option[Long] = {
    db.withConnection { implicit c =>
      SQL(
        """
            insert into pet_search_info
              (
                user_id, name, gender, kind, feature, pref, place,
                image_path, create_time, update_time
              ) values (
                {user_id}, {name}, {gender}, {kind}, {feature}, {pref}, {place},
                {image_path}, NOW(), NOW()
              )
          """).on(
          'user_id -> userId,
          'name -> r.name,
          'gender -> r.gender,
          'kind -> r.petKind,
          'feature -> r.feature,
          'pref -> r.pref,
          'place -> r.place,
          'image_path -> image_path).executeInsert()
    }
  }
}
