package util
import forms.PetInfoRegistorForm
import play.api.cache._

import scala.concurrent.duration._

object CacheUtil {

  def set(cache: SyncCacheApi, key: String, obj: Any): Unit = {
    cache.set(key, obj, 30.minute)
  }

  def get(cache: SyncCacheApi, key: String) = cache.get(key)
  def delete(cashe: SyncCacheApi, key: String) = cashe.remove(key)

  def getPetRegistorForm(cache: SyncCacheApi) = {
    cache.get[PetInfoRegistorForm]("PetInfoRegistorForm")
  }
}
