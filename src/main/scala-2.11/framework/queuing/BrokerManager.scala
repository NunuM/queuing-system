package framework.queuing

/**
  * Created by nuno on 18-08-2016.
  */
trait BrokerManager {
  def sendMessage(message:String) : Unit
  def requestMessage() : String
  def cleanUpConnections() : Unit
}
