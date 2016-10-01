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

  implicit val timeout = Timeout(1 second)  // Timeout for futures.

  val system = ActorSystem("pcb")
  val indRef = system.actorOf(Props[Industry])

  Thread.sleep(2000)

  "An Industry Actor" should "create an Industry correctly" in {
    val future: Future[Int] = ask(
      indRef, 
      CreateIndustry("AM", "Aerospace & Defense", "BM")
    ).mapTo[Int]
    Await.result(future, 1 second) should be (1)  // Number of tuples inserted.
  } 

  "An Industry Actor" should "delete an Industry correctly" in {
    val future: Future[Int] = ask(
      indRef, 
      DeleteIndustry("AM")
    ).mapTo[Int]
    Await.result(future, 1 second) should be (1)  // Number of tuples deleted.
  } 

  override def afterAll() {
     system.terminate
  }
}
