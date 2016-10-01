package com.pcb

import akka.actor.{ActorSystem, Props}
import com.pcb.messages._
import com.pcb.model.Industry
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class IndustryClientTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val system = ActorSystem("pcb")
  val indRef = system.actorOf(Props[Industry])

  Thread.sleep(2000)

  "An IndustryClient" should "add an Industry correctly" in {
    indRef ! CreateIndustry("AM", "Aerospace & Defense", "BM")
    Thread.sleep(2000)
    indRef ! DeleteIndustry("AM")
  } 

  Thread.sleep(2000)

  override def afterAll() {
     system.terminate
  }
}
