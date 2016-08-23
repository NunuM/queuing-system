# Queuing System
Scala application using akka actors to process messages from RabbitMQ

We have one pool of actors in balanced mode to process the data that arrives from the dispatcher actor

Uses RabbitMQ topic to receiving messages based on a pattern

Settings are read from [application configuration file!](https://github.com/NunuM/queuing-system/blob/master/src/main/resources/application.conf)
