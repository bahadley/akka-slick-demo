package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages.{CreateIndustry, CreateStatusType, CreateTaxRate, CreateTradeType}
import scala.concurrent.duration._
import scala.language.postfixOps

class ReferenceConsumer(ref:ReferenceData) extends Actor with ActorLogging with Consumer {
  import context.dispatcher

  implicit val timeout = Timeout(2 seconds)

  val config = context.system.settings.config

  val directory = config.getString("etl.sources.directory")
  val file = config.getString(s"etl.sources.${ref.source}")
  def endpointUri = s"file:${directory}?include=${file}&delete=true"

  val dbPath = config.getString("etl.db.path")
  val data = context.actorSelection(dbPath)

  def receive = {
    case msg: CamelMessage => {
      val lines = msg.bodyAs[String].split("\\r?\\n")
      for (line <- lines) {
        val crInd = ref.genMsg(line.split('|'))
        ask(data, crInd) onFailure {
          case _ => log.error("Creation failed")
        }
      }
    }
  }
}

trait ReferenceData {
  val source = ""
  def genMsg(arr:Array[String]): Any
}

object Industry extends ReferenceData {
  override val source = "industry"
  override def genMsg(arr:Array[String]) = 
    CreateIndustry(arr(0), arr(1), arr(2))
}

object StatusType extends ReferenceData {
  override val source = "statusType"
  override def genMsg(arr:Array[String]) = 
    CreateStatusType(arr(0), arr(1))
}

object TaxRate extends ReferenceData {
  override val source = "taxRate"
  override def genMsg(arr:Array[String]) = 
    CreateTaxRate(arr(0), arr(1), arr(2).toDouble)
}

object TradeType extends ReferenceData {
  override val source = "tradeType"
  override def genMsg(arr:Array[String]) = 
    CreateTradeType(arr(0), arr(1), arr(2).toShort, arr(3).toShort)
}
