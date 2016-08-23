package model

import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}
import akka.routing.BalancingPool
import application.configuration.Config
import framework.queuing.Messaging
import model.core.actor.messages.AskOpinion
import org.apache.log4j.Logger


/**
  * Created by nuno on 17-08-2016.
  */
class CoreActor extends Actor {

  /**
    * In case of child failure, it will be restored
    */
  override val supervisorStrategy = OneForOneStrategy(loggingEnabled = true) {
    case ex: IllegalArgumentException => Resume
    case _: Exception => Restart
  }

  /**
    * MessageDispatcher
    */
  implicit val ec = context.dispatcher

  /**
    * Receive asynchronous messages:
    * Bangs the pool router actor to conceive an opinion.
    *
    * @group messages: ProcessMessage, Any
    * @throws IllegalArgumentException unknown massage
    * @return void
    */
  override def receive: Receive = {
    case ProcessMessage(message, router) =>
      CoreActor.log.info("Delivering work")
      router ! AskOpinion(message)
    case _ => throw new IllegalArgumentException(
      "Received wrong type of message that i (CoreActor) can process")
  }

}

/**
  * Companion CoreActor Class.
  * Static methods will be here.
  */
object CoreActor {
  var isRunning: Boolean = true
  private val system = ActorSystem("BalancedPoolSystem")
  private val dispatcher = system.actorOf(Props[CoreActor], "dispatcher")
  private val router = system.actorOf((new BalancingPool(Config.WORKERS_NUMBER).props(Props[WordCountActor])), "BalancedPool")
  private val log: Logger = Logger.getLogger(getClass.getName)

  /**
    * Uses rabbitmq connection to subscribe to a new chanel, each message received, it will
    * dispatch work to an actor from router pool.
    */
  def startCommunication = {
    log.info("Starting communication")
    var count = 1
    val messagingBroker = Messaging.getBrokerInstance()

    while (isRunning) {
      log.info("Waiting for message from broker")
      assignTask(messagingBroker.requestMessage())
      log.info("Message received and assigned work to one actor")

      if (count >= 50) {
        isRunning = false
      }
      log.info(count + " messages processed")
      count += 1
    }
    log.info("Exit Application")

    stopSystem
    messagingBroker.cleanUpConnections
  }

  /**
    * Bang the dispatcher with work.
    *
    * @param messageToProcess string
    */
  @inline
  private def assignTask(messageToProcess: String): Unit = {
    dispatcher ! ProcessMessage(messageToProcess, router)
  }

  /**
    * Terminate System of Actors.
    */
  @inline
  private def stopSystem = system.terminate()

}


