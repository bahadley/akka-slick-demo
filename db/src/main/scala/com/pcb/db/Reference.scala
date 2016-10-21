package com.pcb.db

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import com.pcb.messages._
import scala.concurrent.duration._
import scala.language.postfixOps
import slick.driver.H2Driver.api._

class Reference extends Actor with ActorLogging {
  import context.dispatcher

  var db = None : Option[Database]

  override def preStart(): Unit = {
    db = Some(Database.forConfig("tpcdi"))
    super.preStart()
  }

  override def postStop(): Unit = {
    db match {
      case None => throw new NullPointerException(ERR_MSG_DB)
      case Some(db) => db.close 
    }
    super.postStop()
  }

  val bkr = new Ocb(context.system.scheduler)

  def receive = {
    case msg: CreateIndustry =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(insertIn(msg))) pipeTo sender()
      }
    case msg: CreateStatusType =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(insertSt(msg))) pipeTo sender()
      }
    case msg: CreateTaxRate =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(insertTx(msg))) pipeTo sender()
      }
    case msg: CreateTradeType =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(insertTt(msg))) pipeTo sender()
      }
    case msg: DeleteIndustry =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB)
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(deleteIn(msg))) pipeTo sender()
      }
    case msg: CountIndustry =>
      db match {
        case None => 
          throw new NullPointerException(ERR_MSG_DB)
        case Some(db) => 
          bkr.withCircuitBreaker(db.run(countIn(msg))) pipeTo sender()
      }
  }

  def insertIn(in: CreateIndustry): DBIO[Int] =
    sqlu"""insert into industry (in_id, in_name, in_sc_id)
      values (${in.in_id}, ${in.in_name}, ${in.in_sc_id})"""

  def deleteIn(in: DeleteIndustry): DBIO[Int] =
    sqlu"delete from industry where in_id = ${in.in_id}"

  def countIn(in: CountIndustry): DBIO[Int] =
    sql"""select count(*) from industry 
      where in_id = ${in.in_id}""".as[Int].head

  def insertSt(st: CreateStatusType): DBIO[Int] =
    sqlu"""insert into statustype (st_id, st_name)
      values (${st.st_id}, ${st.st_name})"""

  def insertTx(tx: CreateTaxRate): DBIO[Int] =
    sqlu"""insert into taxrate (tx_id, tx_name, tx_rate)
      values (${tx.tx_id}, ${tx.tx_name}, ${tx.tx_rate})"""

  def insertTt(tt: CreateTradeType): DBIO[Int] =
    sqlu"""insert into tradetype (tt_id, tt_name, tt_is_sell, tt_is_mrkt)
      values (${tt.tt_id}, ${tt.tt_name}, ${tt.tt_is_sell}, ${tt.tt_is_market})"""

  val ERR_MSG_DB = "Connection pool is uninitialized" 
}
