package net.petitviolet.sandbox.z

import net.petitviolet.sandbox.Logging

import scalaz._
import Scalaz._
import scalaz.concurrent.Task

object TaskPrac extends App with Logging {
  private def withSleep[A](a: => A): A = {
    Thread.sleep(500)
    a
  }
  val t: Task[Int] = Task(withSleep{
    1 + 2 + 3
  })
  logging(t)
  logging(t.unsafePerformSync)
  logging(t.unsafePerformSyncAttempt)
  t.unsafePerformAsync(logging)

  val attempt: Task[Throwable \/ Int] = t.attempt
  logging(attempt)

  logging(t.get)

  logging("-------------")

}

