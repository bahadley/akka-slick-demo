package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.pipe
import com.pcb.messages.CreateIndustry
import scala.io.Source

class Industry extends Actor with ActorLogging with Consumer {

  def endpointUri = "file:data/input?delete=true"

  def receive = {
    case msg: CamelMessage => {
      //println("received %s" format msg.bodyAs[String])
      
      val lines = msg.bodyAs[String].split("\\r?\\n")
      lines foreach println
    }
  }

  def parseRow(arr:Array[String]) = CreateIndustry(arr(0), arr(1), arr(2))
}
