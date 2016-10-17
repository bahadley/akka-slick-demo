package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages.{CreateIndustry, CreateStatusType, CreateTaxRate, CreateTradeType}
import scala.concurrent.duration._
import scala.language.postfixOps

class Reference(source:String, mb:MessageBuilder) extends Actor with ActorLogging with Consumer {

  import context.dispatcher

  implicit val timeout = Timeout(2 seconds)

  val config = context.system.settings.config

  val directory = config.getString("etl.sources.directory")
  val file = config.getString(s"etl.sources.${source}")
  def endpointUri = s"file:${directory}?include=${file}&delete=true"

  val dbPath = config.getString("etl.db.path")
  val data = context.actorSelection(dbPath)

  val NEW_LINE = "\\r?\\n"
  val DELIM = '|'

  def receive = {
    case msg: CamelMessage => {
      val lines = msg.bodyAs[String].split(NEW_LINE)
      for (line <- lines) {
        val crInd = mb.genMsg(line.split(DELIM))
        ask(data, crInd) onFailure {
          case _ => log.error("Creation failed")
        }
      }
    }
  }
}

trait MessageBuilder {
  def genMsg(arr:Array[String]): Any
}

object Industry extends MessageBuilder {
  def genMsg(arr:Array[String]) = CreateIndustry(arr(0), arr(1), arr(2))
}

object StatusType extends MessageBuilder {
  def genMsg(arr:Array[String]) = CreateStatusType(arr(0), arr(1))
}

object TaxRate extends MessageBuilder {
  def genMsg(arr:Array[String]) = CreateTaxRate(arr(0), arr(1), arr(2).toDouble)
}

object TradeType extends MessageBuilder {
  def genMsg(arr:Array[String]) = CreateTradeType(arr(0), arr(1), arr(2).toShort, arr(3).toShort)
}
