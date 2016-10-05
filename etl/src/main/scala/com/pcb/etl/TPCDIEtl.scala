package com.pcb.etl

import akka.actor._
import akka.event.LoggingReceive
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.language.postfixOps

class TPCDIEtl extends Actor with ActorLogging {

  import akka.actor.SupervisorStrategy._

  override val supervisorStrategy = 
    OneForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1 minute) {
        case _: NullPointerException  => Restart
        case _: Exception             => Escalate
        case _                        => Escalate
      }

  override def preStart(): Unit = {
    context.actorOf(Props[Industry], "industry")
  }

  def receive = {
    case _ =>
  }
}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("pcb-etl")
  val actor = system.actorOf(Props[TPCDIEtl], name = "tpcdietl")

  log.info("Actor started; has path: [{}]", actor.path)
}
