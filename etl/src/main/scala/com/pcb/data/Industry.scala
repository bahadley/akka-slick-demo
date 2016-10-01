package com.pcb.model 

import akka.actor.{Actor, ActorLogging}
import com.pcb.messages.{CreateIndustry, DeleteIndustry}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import slick.driver.H2Driver.api._

class Industry extends Actor with ActorLogging {

  def insert(i: CreateIndustry): DBIO[Int] =
      sqlu"insert into industry values (${i.in_id}, ${i.in_name}, ${i.in_sc_id})"

  def delete(i: DeleteIndustry): DBIO[Int] =
      sqlu"delete from industry where in_id = ${i.in_id}"

  val db = Database.forConfig("tpcdi")

  def receive = {
    case msg: CreateIndustry =>
      val f: Future[_] = {
        db.run(insert(msg))
      }
      Await.result(f, Duration.Inf)
    case msg: DeleteIndustry =>
      val f: Future[_] = {
        db.run(delete(msg))
      }
      Await.result(f, Duration.Inf)
  }
}
