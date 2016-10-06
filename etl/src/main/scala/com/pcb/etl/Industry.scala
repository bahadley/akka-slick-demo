package com.pcb.etl

import akka.actor.{Actor, ActorLogging}
import akka.camel.{CamelMessage, Consumer}
import akka.pattern.pipe
import com.pcb.messages.CreateIndustry

class Industry extends Actor with ActorLogging with Consumer {

  val fileName = "Industry.txt"

  def endpointUri = s"file:data/input?include=${fileName}&delete=true"

  def receive = {
    case msg: CamelMessage => {
      val lines = msg.bodyAs[String].split("\\r?\\n")
      for (line <- lines) {
        val v = line.split('|')
        println(genMsg(v))
      }
    }
  }

  def genMsg(arr:Array[String]) = 
    CreateIndustry(arr(0), arr(1), arr(2))
}
