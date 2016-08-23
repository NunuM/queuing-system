package model

import akka.actor.ActorRef

/**
  * Created by nuno on 17-08-2016.
  */
case class ProcessMessage(message:String, router: ActorRef)
