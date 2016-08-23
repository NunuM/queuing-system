# Queuing Application
Scala application using akka actors to process messages from rabbitmq

We have one pool of actors in balanced mode, to process the data, that arrives from the dispatcher actor.
Uses RabbitMQ topic to receiving messages based on a pattern.
