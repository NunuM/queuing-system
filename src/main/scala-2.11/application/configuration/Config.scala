package application.configuration

import com.typesafe.config.ConfigFactory

/**
  * Created by nuno on 17-08-2016.
  */
object Config {

  val WORKERS_NUMBER = ConfigFactory.load().getInt("application.workers.number")
  val MESSAGING_FRAMEWORK = ConfigFactory.load().getString("application.messaging.system")

  val RABBITMQ_URI = ConfigFactory.load().getString("rabbitmq.server.amqp")
  val RABBITMQ_EXCHANGE_TYPE = ConfigFactory.load().getString("rabbitmq.exchange.type")

  val RABBITMQ_PUBLISHER_QUEUE = ConfigFactory.load().getString("rabbitmq.publisher.queue")
  val RABBITMQ_PUBLISHER_EXCHANGE = ConfigFactory.load().getString("rabbitmq.publisher.exchange")
  val RABBITMQ_PUBLISHER_ROUTING_KEY = ConfigFactory.load().getString("rabbitmq.publisher.routing.key")

  val RABBITMQ_CONSUMER_QUEUE = ConfigFactory.load().getString("rabbitmq.consumer.queue")
  val RABBITMQ_CONSUMER_EXCHANGE = ConfigFactory.load().getString("rabbitmq.consumer.exchange")
  val RABBITMQ_CONSUMER_ROUTING_KEY = ConfigFactory.load().getString("rabbitmq.consumer.routing.key")

}
