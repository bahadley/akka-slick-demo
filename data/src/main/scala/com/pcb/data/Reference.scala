package com.pcb.data

import akka.actor.{Actor, ActorLogging}
import akka.pattern.pipe
import com.pcb.messages._
import slick.driver.H2Driver.api._

class Reference extends Actor with ActorLogging {

  import context.dispatcher

  var db = None : Option[Database]
  val ERR_MSG_DB = "Connection pool is uninitialized" 

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

  def receive = {
    case msg: CreateIndustry =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => pipe(db.run(insertIn(msg))) to sender()
      }
    case msg: CreateStatusType =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => pipe(db.run(insertSt(msg))) to sender()
      }
    case msg: CreateTaxRate =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => pipe(db.run(insertTx(msg))) to sender()
      }
    case msg: CreateTradeType =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB) 
        case Some(db) => pipe(db.run(insertTt(msg))) to sender()
      }
    case msg: DeleteIndustry =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB)
        case Some(db) => pipe(db.run(deleteIn(msg))) to sender()
      }
    case msg: CountIndustry =>
      db match {
        case None => throw new NullPointerException(ERR_MSG_DB)
        case Some(db) => pipe(db.run(countIn(msg))) to sender()
      }
  }

  def insertIn(in: CreateIndustry): DBIO[Int] =
    sqlu"insert into industry values (${in.in_id}, ${in.in_name}, ${in.in_sc_id})"

  def deleteIn(in: DeleteIndustry): DBIO[Int] =
    sqlu"delete from industry where in_id = ${in.in_id}"

  def countIn(in: CountIndustry): DBIO[Int] =
    sql"select count(*) from industry where in_id = ${in.in_id}".as[Int].head

  def insertSt(st: CreateStatusType): DBIO[Int] =
    sqlu"insert into statustype values (${st.st_id}, ${st.st_name})"

  def insertTx(tx: CreateTaxRate): DBIO[Int] =
    sqlu"insert into taxrate values (${tx.tx_id}, ${tx.tx_name}, ${tx.tx_rate})"

  def insertTt(tt: CreateTradeType): DBIO[Int] =
    sqlu"insert into tradetype values (${tt.tt_id}, ${tt.tt_name}, ${tt.tt_is_sell}, ${tt.tt_is_market})"
}
