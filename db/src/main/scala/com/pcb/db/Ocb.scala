package com.pcb.db

import akka.actor.Scheduler
import akka.pattern.CircuitBreaker
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

class Ocb (scheduler: Scheduler)(implicit executor: ExecutionContext) {

  val bkr =
    new CircuitBreaker(
      scheduler,
      maxFailures = 5,
      callTimeout = 2.seconds,
      resetTimeout = 1.minute)

  def withCircuitBreaker[T](body: => Future[T]): Future[T] = bkr.withCircuitBreaker(body)
}
