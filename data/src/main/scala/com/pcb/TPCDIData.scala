package com.pcb

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.ask
import com.pcb.messages._
import scala.concurrent.duration._
import scala.language.postfixOps

class TPCDIData extends Actor with ActorLogging {

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
    case msg: CreateIndustry =>
      indRef forward msg
    case msg: DeleteIndustry =>
      indRef forward msg
    case msg: CountIndustry =>
      indRef forward msg
  }
}
