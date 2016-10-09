package com.pcb.data

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

class TPCDIDataTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  implicit val timeout = Timeout(2 seconds)  // Timeout for futures.

  val system = ActorSystem("pcb")
  val dataRef = system.actorOf(Props[TPCDIData])

  val industry = Vector("AM", "Aerospace & Defense", "BM")

  "An Industry Actor" should "create an Industry tuple" in {
    val f1: Future[Int] = 
      ask(
        dataRef, 
        CreateIndustry(industry(0), industry(1), industry(2))).mapTo[Int]
    Await.result(f1, timeout.duration) should be (1)  // Number of tuples inserted.

    val f2: Future[Int] = 
      ask(
        dataRef, 
        CountIndustry(industry(0))).mapTo[Int]
    Await.result(f2, timeout.duration) should be (1) 
  } 

  "An Industry Actor" should "delete an Industry tuple" in {
    val f1: Future[Int] = 
      ask(
        dataRef, 
        DeleteIndustry(industry(0))).mapTo[Int]
    Await.result(f1, timeout.duration) should be (1)  // Number of tuples deleted.

    val f2: Future[Int] = 
      ask(
        dataRef, 
        CountIndustry(industry(0))).mapTo[Int]
    Await.result(f2, timeout.duration) should be (0) 
  } 

  override def afterAll() {
     system.terminate
  }
}
