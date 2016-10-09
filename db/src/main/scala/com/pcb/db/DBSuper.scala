package com.pcb.db

import akka.actor._
import akka.event.LoggingReceive
import akka.actor.SupervisorStrategy._
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.language.postfixOps

class DBSuper extends Actor with ActorLogging {

  override val supervisorStrategy = 
    OneForOneStrategy(
      maxNrOfRetries = 5,
      withinTimeRange = 1 minute) {
        case _: NullPointerException  => Restart
        case _: Exception             => Escalate
      }

  override def preStart(): Unit = {
    context.actorOf(Props[Reference], "db-reference")
  }

  def receive = {
    case _ =>
  }
}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("pcb-db")
  val actor = system.actorOf(Props[DBSuper], name = "db-super")

  log.info("Actor started; has path: [{}]", actor.path)
}
