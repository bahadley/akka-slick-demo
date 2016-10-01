package com.pcb.model 


import akka.actor.{Actor, ActorLogging}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

import slick.driver.H2Driver.api._


case class Add(in_id: String, in_name: String, in_sc_id: String)


class Industry extends Actor with ActorLogging {

  def insert(i: Add): DBIO[Int] =
      sqlu"insert into industry values (${i.in_id}, ${i.in_name}, ${i.in_sc_id})"

  val db = Database.forConfig("tpcdi")

  def receive = {
    case msg: Add =>

      val f: Future[_] = {
        db.run(insert(msg))
      }
      Await.result(f, Duration.Inf)
  }
}
