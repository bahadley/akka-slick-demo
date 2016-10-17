package com.pcb.etl

import akka.actor._
import akka.event.LoggingReceive
import org.slf4j.LoggerFactory
import scala.concurrent.duration._
import scala.language.postfixOps

class EtlSuper extends Actor with ActorLogging {

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
    context.actorOf(Props(classOf[Reference], "industry", Industry), "etl-industry")
    context.actorOf(Props(classOf[Reference], "statusType", StatusType), "etl-statusType")
    context.actorOf(Props(classOf[Reference], "taxRate", TaxRate), "etl-taxRate")
    context.actorOf(Props(classOf[Reference], "tradeType", TradeType), "etl-tradeType")
  }

  def receive = {
    case _ =>
  }
}

object Main extends App {

  val log = LoggerFactory.getLogger(Main.getClass)

  val system = ActorSystem("pcb-etl")
  val actor = system.actorOf(Props[EtlSuper], name = "etl-super")

  log.info("Actor started; has path: [{}]", actor.path)
}
