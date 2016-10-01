package com.pcb.data 

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import com.pcb.messages.{CreateIndustry, DeleteIndustry}
import slick.driver.H2Driver.api._

class Industry extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  def insert(i: CreateIndustry): DBIO[Int] =
      sqlu"insert into industry values (${i.in_id}, ${i.in_name}, ${i.in_sc_id})"

  def delete(i: DeleteIndustry): DBIO[Int] =
      sqlu"delete from industry where in_id = ${i.in_id}"

  var db = None : Option[Database]

  override def preStart(): Unit = {
    db = Some(Database.forConfig("tpcdi"))
    super.preStart()
  }

  def receive = {
    case msg: CreateIndustry =>
      db match {
        case None => log.error("Invalid database reference")
        case Some(db) => pipe(db.run(insert(msg))) to sender()
      }
    case msg: DeleteIndustry =>
      db match {
        case None => log.error("Invalid database reference")
        case Some(db) => pipe(db.run(delete(msg))) to sender()
      }
  }

  override def postStop(): Unit = {
    db match {
      case None => log.error("Invalid database reference")
      case Some(db) => db.close 
    }
    super.postStop()
  }
}
