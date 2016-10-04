package com.pcb.data

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import com.pcb.messages.{CountIndustry, CreateIndustry, DeleteIndustry}
import slick.driver.H2Driver.api._

class Industry extends Actor with ActorLogging {

  import scala.concurrent.ExecutionContext.Implicits.global

  var db = None : Option[Database]

  override def preStart(): Unit = {
    db = Some(Database.forConfig("tpcdi"))
    super.preStart()
  }

  def receive = {
    case msg: CreateIndustry =>
      db match {
        case None => throw new NullPointerException("Database is uninitialized") 
        case Some(db) => pipe(db.run(insert(msg))) to sender()
      }
    case msg: DeleteIndustry =>
      db match {
        case None => throw new NullPointerException("Database is uninitialized")
        case Some(db) => pipe(db.run(delete(msg))) to sender()
      }
    case msg: CountIndustry =>
      db match {
        case None => throw new NullPointerException("Database is uninitialized")
        case Some(db) => pipe(db.run(count(msg))) to sender()
      }
  }

  override def postStop(): Unit = {
    db match {
      case None => throw new NullPointerException("Database is uninitialized")
      case Some(db) => db.close 
    }
    super.postStop()
  }

  def insert(i: CreateIndustry): DBIO[Int] =
    sqlu"insert into industry values (${i.in_id}, ${i.in_name}, ${i.in_sc_id})"

  def delete(i: DeleteIndustry): DBIO[Int] =
    sqlu"delete from industry where in_id = ${i.in_id}"

  def count(i: CountIndustry): DBIO[Int] =
    sql"select count(*) from industry where in_id = ${i.in_id}".as[Int].head
}
