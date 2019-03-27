package services

import java.io.File

import play.api.Logger
import twitter4j._

object TwitterService {

  val logger = Logger(this.getClass)

  def postTweet(msg: String): Status = {
    logger.info("New Tweet:" + msg)

    if (msg == null || msg.length < 0) {
      logger.error("Tweet message is nothing.....")
    }

    val twitter = TwitterFactory.getSingleton
    try {
      val token = twitter.getOAuthRequestToken
      logger.debug("token:" + token.getToken)
      logger.debug("secretToken:" + token.getTokenSecret)
    } catch {
      case e: Exception => {}
    }

    val status = twitter.updateStatus(msg)
    logger.info("Successfully updated the status to [" + status.getText() + "].")
    status
  }

  def postImageTweet(msg: String, file: File): Status = {
    logger.info("New Tweet:" + msg)

    if (msg == null || msg.length < 0) {
      logger.error("Tweet message is nothing.....")
    }

    val twitter = TwitterFactory.getSingleton
    try {
      val token = twitter.getOAuthRequestToken
      logger.debug("token:" + token.getToken)
      logger.debug("secretToken:" + token.getTokenSecret)
    } catch {
      case e: Exception => {}
    }

    if (file != null && file.exists()) {
      val status = twitter.updateStatus(new StatusUpdate(msg).media(file))
      logger.info("Successfully updated the status to [" + status.getText() + "].")
      status
    } else {
      val status = twitter.updateStatus(msg)
      logger.info("Successfully updated the status to [" + status.getText() + "].")
      status
    }
  }
}
