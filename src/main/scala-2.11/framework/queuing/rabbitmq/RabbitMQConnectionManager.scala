package framework.queuing.rabbitmq


import application.configuration.Config
import com.rabbitmq.client.{Connection, ConnectionFactory, QueueingConsumer}
import framework.queuing.BrokerManager
import org.apache.log4j.Logger

/**
  * Created by nuno on 17-08-2016.
  */
object RabbitMQConnectionManager extends BrokerManager {
  private var connection: Connection = null
  private var publisher: RabbitMQPublisher = null
  private var subscriber: RabbitMQSubscriber = null
  private var consumer: QueueingConsumer = null
  private val log: Logger = Logger.getLogger(getClass.getName)


  /**
    * Create and declare publish exchange(postman) with config settings.
    *
    * @return RabbitMQPublisher
    */
  private def getPublishChannel: RabbitMQPublisher = {
    publisher match {
      case null =>
        val channel = getConnection().createChannel()

        channel.exchangeDeclare(
          Config.RABBITMQ_PUBLISHER_EXCHANGE,
          Config.RABBITMQ_EXCHANGE_TYPE,
          true)

        log.info(
          "Created Publisher exchanger: " +
            Config.RABBITMQ_PUBLISHER_EXCHANGE +
            ", type: " + Config.RABBITMQ_EXCHANGE_TYPE
        )

        publisher = RabbitMQPublisher(channel)
        publisher
      case _ => publisher
    }

  }

  /**
    * Send a message to publisher.
    *
    * @param message
    */
  override def sendMessage(message: String): Unit = {

    this.getPublishChannel.channel.basicPublish(
      Config.RABBITMQ_PUBLISHER_EXCHANGE,
      Config.RABBITMQ_PUBLISHER_ROUTING_KEY,
      null,
      message.getBytes())
  }

  /**
    * Blocks until receive a new message from subscriber exchange.
    *
    * @return String
    */
  override def requestMessage(): String = {
    getSubscriberChannel
    new String(consumer.nextDelivery().getBody)
  }


  /**
    * Create and declare publish exchange(postman) with config settings.
    *
    * @return RabbitMQSubscriber
    */
  private def getSubscriberChannel: RabbitMQSubscriber = {
    subscriber match {
      case null =>
        val channel = getConnection().createChannel()
        channel.exchangeDeclare(
          Config.RABBITMQ_CONSUMER_EXCHANGE,
          Config.RABBITMQ_EXCHANGE_TYPE,
          true)

        log.info(
          "Created Subscriber exchanger: " +
            Config.RABBITMQ_CONSUMER_EXCHANGE +
            ", type: " + Config.RABBITMQ_EXCHANGE_TYPE
        )

        consumer = new QueueingConsumer(channel)
        channel.basicConsume(Config.RABBITMQ_CONSUMER_QUEUE, true, consumer)
        subscriber = RabbitMQSubscriber(channel)
        subscriber
      case _ => subscriber
    }

  }

  /**
    * Return a connection if one doesn't exist.
    * Else create a new one.
    *
    * @note It is a singleton method.
    *
    */
  private def getConnection(): Connection = {
    connection match {
      case null => {
        val factory = new ConnectionFactory();
        factory.setUri(Config.RABBITMQ_URI);
        connection = factory.newConnection();
        connection
      }
      case _ => connection
    }
  }

  /**
    * Close subscriber channel
    */
  def closeSubscriberChannel = subscriber.channel.close()

  /**
    * Close publisher channel
    */
  def closePublisherChannel = publisher.channel.close


  /**
    * Close RabbitMQ connection
    */
  @inline
  def closeConnection = connection.close()

  /**
    * Closes all connections
    */
  override def cleanUpConnections = {
    closeSubscriberChannel
    closePublisherChannel
    closeConnection
  }
}
