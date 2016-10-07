package com.pcb.data

import akka.actor._
import akka.event.LoggingReceive
import akka.actor.SupervisorStrategy._
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.language.postfixOps

class DataSuper extends Actor with ActorLogging {

  override val supervisorStrategy = 
    OneForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1 minute) {
        case _: NullPointerException  => Restart
        case _: Exception             => Escalate
      }

  override def preStart(): Unit = {
    context.actorOf(Props[ReferenceData], "data-reference")
  }

  def receive = {
    case _ =>
  }
}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("pcb-data")
  val actor = system.actorOf(Props[DataSuper], name = "data-super")

  log.info("Actor started; has path: [{}]", actor.path)
}
