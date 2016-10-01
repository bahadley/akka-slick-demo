package com.pcb


import akka.actor.{ActorSystem, Props}

import com.pcb.model._

import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}


class IndustryClientTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  val system = ActorSystem("pcb")
  val indRef = system.actorOf(Props[Industry])

  "An IndustryClient" should "add an Industry correctly" in {
    indRef ! Add("AM", "Aerospace & Defense", "BM")
    Thread.sleep(2000)
  } 

  override def afterAll() {
     system.terminate
  }
}
