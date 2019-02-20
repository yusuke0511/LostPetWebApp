package forms

case class PetInfoRegistorForm(
  name: String,
  gender: Int,
  petKind: Int,
  feature: String,
  pref: Int,
  place: String,
  lat: String,
  lng: String)