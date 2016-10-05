package com.pcb.etl

import akka.actor._
import akka.event.LoggingReceive
import scala.concurrent.duration._
import scala.language.postfixOps

class TPCDIEtl extends Actor with ActorLogging {

  import akka.actor.SupervisorStrategy._

  val indRef = context.actorOf(Props[Industry], "Industry")

  override val supervisorStrategy = 
    OneForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1 minute) {
        case _: NullPointerException  => Restart
        case _: Exception             => Escalate
      }

  def receive = {
    case _ =>
  }
}
