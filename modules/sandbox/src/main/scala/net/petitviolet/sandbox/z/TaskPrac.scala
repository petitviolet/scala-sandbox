package net.petitviolet.sandbox.z

import net.petitviolet.sandbox.Logging

import scalaz._
import Scalaz._
import scalaz.concurrent.Task
import scala.concurrent.duration._

object TaskPrac extends App with Logging {
  private def withSleep[A](a: => A): A = {
    Thread.sleep(100)
    a
  }
  val t: Task[Int] = Task(withSleep{ 1 + 2 + 3 })
  logging(t)
  logging(t.unsafePerformSync)
  logging(t.unsafePerformSyncAttempt)
  t.unsafePerformAsync(logging)

  val attempt: Task[Throwable \/ Int] = t.attempt
  logging(attempt)

  logging(t.get)

  logging("-------------")

  private def successTask: Task[Int] = for {
    i <- Task(withSleep(1))
    j <- Task(withSleep(2))
    k <- Task(withSleep(3))
  } yield {
    i * j * k
  }
  private def willFail = successTask.flatMap { i => Task(withSleep(i / 0)) }

  private def handler[A](a: => A): PartialFunction[Throwable, A] = { case t: Throwable => a }

  logging(willFail.attempt.unsafePerformSync)
  logging(willFail.flatMap { i => Task(withSleep(i / 0)) }.handle(handler(100)).attempt.unsafePerformSync)

  logging(successTask.retry(List.fill(3)(10.millis), { t => logging(t); true }).attempt.unsafePerformSync)
  logging(willFail.retry(List.fill(3)(10.millis), { t => logging(t); true }).attempt.unsafePerformSync)

  logging(successTask.timed(10.millis).attempt.unsafePerformSync)
  logging(willFail.timed(10.millis).attempt.unsafePerformSync)

  logging(willFail.timed(10.millis).attempt.unsafePerformSync)
}

