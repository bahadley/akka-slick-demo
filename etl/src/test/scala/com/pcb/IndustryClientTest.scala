package com.pcb

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.pcb.messages._
import com.pcb.data.Industry
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.language.postfixOps

class IndustryClientTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  implicit val timeout = Timeout(2 seconds)  // Timeout for futures.

  val system = ActorSystem("pcb")
  val indRef = system.actorOf(Props[Industry])

  "An Industry Actor" should "create an Industry correctly" in {
    val f1: Future[Int] = 
      ask(
        indRef, 
        CreateIndustry("AM", "Aerospace & Defense", "BM")).mapTo[Int]
    Await.result(f1, 2 seconds) should be (1)  // Number of tuples inserted.

    val f2: Future[Option[Int]] = 
      ask(
        indRef, 
        CountIndustry("AM")).mapTo[Option[Int]]
    Await.result(f2, 2 seconds) match {
      case None => println("Failed")
      case Some(count) => count should be (1)
    }
  } 

  "An Industry Actor" should "delete an Industry correctly" in {
    val f1: Future[Int] = 
      ask(
        indRef, 
        DeleteIndustry("AM")).mapTo[Int]
    Await.result(f1, 2 seconds) should be (1)  // Number of tuples deleted.

    val f2: Future[Option[Int]] = 
      ask(
        indRef, 
        CountIndustry("AM")).mapTo[Option[Int]]
    Await.result(f2, 2 seconds) match {
      case None => println("Failed")
      case Some(count) => count should be (0)
    }
  } 

  override def afterAll() {
     system.terminate
  }
}
