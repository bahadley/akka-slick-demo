package com.pcb.data

import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.ask
import com.pcb.messages._
import org.slf4j.LoggerFactory
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

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("pcb-data")
  val actor = system.actorOf(Props[TPCDIData], name = "tpcdidata")

  log.info("Actor started; has path: [{}]", actor.path)
}
