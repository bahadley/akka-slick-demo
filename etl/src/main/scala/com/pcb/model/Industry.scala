package com.pcb.model 

import akka.actor.{Actor, ActorLogging}
import com.pcb.messages.AddIndustry
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import slick.driver.H2Driver.api._

class Industry extends Actor with ActorLogging {

  def insert(i: AddIndustry): DBIO[Int] =
      sqlu"insert into industry values (${i.in_id}, ${i.in_name}, ${i.in_sc_id})"

  val db = Database.forConfig("tpcdi")

  def receive = {
    case msg: AddIndustry =>
      val f: Future[_] = {
        db.run(insert(msg))
      }
      Await.result(f, Duration.Inf)
  }
}
