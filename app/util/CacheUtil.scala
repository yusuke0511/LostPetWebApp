package util
import forms.PetInfoRegistorForm
import models.{ ImagePath, User }
import play.api.Logger
import play.api.cache._

import scala.concurrent.duration._

object CacheUtil {

  val log = Logger(this.getClass)

  val KEY_PetInfoRegistorForm = "PetInfoRegistorForm"
  val KEY_LoginInfo = "LoginInfo"
  val KEY_ImagePath = "ImagePath"

  def set(cache: SyncCacheApi, key: String, obj: Any): Unit = {
    cache.set(key, obj, 30.minute)
    log.debug("Cache Key => " + key + " : Cache Value => " + obj)
  }

  def get(cache: SyncCacheApi, key: String) = cache.get(key)
  def delete(cache: SyncCacheApi, key: String) = cache.remove(key)

  def deletePetRegistInfoCache(cache: SyncCacheApi, uuid: String) = {
    cache.remove(uuid + KEY_PetInfoRegistorForm)
    cache.remove(uuid + KEY_ImagePath)
  }

  def getPetRegistorForm(cache: SyncCacheApi, uuid: String) = {
    cache.get[PetInfoRegistorForm](uuid + KEY_PetInfoRegistorForm)
  }

  def getLoginInfo(cache: SyncCacheApi, uuid: String) = {
    cache.get[User](uuid + KEY_LoginInfo)
  }

  def getImagePath(cache: SyncCacheApi, uuid: String) = {
    cache.get[ImagePath](uuid + KEY_ImagePath)
  }
}
