package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages.CreateTradeType
import scala.concurrent.duration._
import scala.language.postfixOps

class TradeType extends Actor with ActorLogging with Consumer {

  import context.dispatcher

  implicit val timeout = Timeout(2 seconds)

  val settings = Settings(context.system)

  def endpointUri = s"file:${settings.directory}?include=${settings.tradeTypeFile}&delete=true"

  val data = context.actorSelection(settings.dataPath)

  val NEW_LINE = "\\r?\\n"
  val DELIM = '|'

  def receive = {
    case msg: CamelMessage => {
      val lines = msg.bodyAs[String].split(NEW_LINE)
      for (line <- lines) {
        val crTt = genMsg(line.split(DELIM))
        ask(data, crTt) onFailure {
          case _ => log.error("TradeType creation failed") 
        }
      }
    }
  }

  def genMsg(arr:Array[String]) = CreateTradeType(arr(0), arr(1), arr(2).toShort, arr(3).toShort)
}
