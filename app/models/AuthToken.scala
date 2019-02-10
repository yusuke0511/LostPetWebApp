package models

import java.util.UUID

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import reactivemongo.play.json._
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.json._ // JSON library
import play.api.libs.json.Reads._ // Custom validation helpers
import play.api.libs.functional.syntax._ // Combinator syntax

/**
 * A token to authenticate a user against an endpoint for a short time period.
 *
 * @param id The unique token ID.
 * @param userID The unique ID of the user the token is associated with.
 * @param expiry The date-time the token expires.
 */
case class AuthToken(
  id: UUID,
  userID: UUID,
  expiry: DateTime)

object AuthToken {
  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  val jodaDateReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))))

  val jodaDateWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }

  val tokenReads: Reads[AuthToken] = (
    (JsPath \ "id").read[UUID] and
    (JsPath \ "userID").read[UUID] and
    (JsPath \ "expiry").read[DateTime](jodaDateReads))(AuthToken.apply _)

  val tokenWrites: Writes[AuthToken] = (
    (JsPath \ "id").write[UUID] and
    (JsPath \ "userID").write[UUID] and
    (JsPath \ "expiry").write[DateTime](jodaDateWrites))(unlift(AuthToken.unapply _))

  //  implicit val jsonFormat: Format[AuthToken] = Format(tokenReads, tokenWrites)
  implicit val jsonFormat = Json.format[AuthToken]
}