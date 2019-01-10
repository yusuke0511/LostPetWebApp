package util

import com.typesafe.config.{Config, ConfigFactory, ConfigValue}

object MessageUtil {

  def getPropetyMessage(key:String):String = {
    val config = ConfigFactory.load("message")
    return  config.getString(key)
  }
}
