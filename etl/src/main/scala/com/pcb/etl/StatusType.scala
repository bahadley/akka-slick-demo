package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages.CreateStatusType
import scala.concurrent.duration._
import scala.language.postfixOps

class StatusType extends Actor with ActorLogging with Consumer {

  import context.dispatcher

  implicit val timeout = Timeout(2 seconds)

  val settings = Settings(context.system)

  def endpointUri = s"file:${settings.directory}?include=${settings.statusTypeFile}&delete=true"

  val data = context.actorSelection(settings.dataPath)

  val NEW_LINE = "\\r?\\n"
  val DELIM = '|'

  def receive = {
    case msg: CamelMessage => {
      val lines = msg.bodyAs[String].split(NEW_LINE)
      for (line <- lines) {
        val crSt = genMsg(line.split(DELIM))
        ask(data, crSt) onFailure {
          case _ => log.error("StatusType creation failed") 
        }
      }
    }
  }

  def genMsg(arr:Array[String]) = CreateStatusType(arr(0), arr(1))
}
