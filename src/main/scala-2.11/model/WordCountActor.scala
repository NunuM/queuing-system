package model

import akka.actor.Actor
import framework.queuing.Messaging
import model.core.actor.messages.AskOpinion
import org.apache.log4j.Logger

/**
  * Created by nuno on 17-08-2016.
  */
class WordCountActor extends Actor {

  private val log: Logger = Logger.getLogger(getClass.getName)

  override def receive: Receive = {
    case AskOpinion(message) =>
      log.info("Opinion asked for the fallowed message:" + message)

      /**
        * @todo implement workload
        */
      log.info("Opinion generated for the fallowed message:" + message)
      Messaging.getBrokerInstance().sendMessage(message)
    case _ => throw new IllegalArgumentException("Not an option requested")
  }
}


