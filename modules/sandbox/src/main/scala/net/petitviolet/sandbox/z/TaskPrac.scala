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
  val a = Task.delay(withSleep(100))
  logging(a)
  logging(a.unsafePerformSync)
//
//  Task.apply { 100 }.unsafePerformAsync(logging)
//  Task.apply { sys.error("fail"); 100 }.unsafePerformAsync(logging)
//
//  logging(Task.apply { 100 }.unsafePerformSyncAttempt)
//  logging(Task.apply { sys.error("fail"); 100 }.unsafePerformSyncAttempt)
//
//  logging(Task.apply { 100 }.unsafePerformSync)
//  logging(Task.apply { sys.error("fail"); 100 }.unsafePerformSync)

//  import scala.concurrent.duration._
//  val d = 10.millis
////  logging(Task { 100 }.unsafePerformSyncFor(d))
////  logging(Task { Thread.sleep(1000); 100 }.unsafePerformSyncFor(d))
//
//  logging(Task { 100 }.unsafePerformSyncAttemptFor(d))
//  logging(Task { Thread.sleep(1000); 100 }.unsafePerformSyncAttemptFor(d))
//
//  Task { sys.error("fail") }.handle { case t: Throwable => "success" }.unsafePerformAsync(logging)
//
//  Thread.sleep(1000)
//  val t: Task[Int] = Task(withSleep{ 1 + 2 + 3 })
//  logging(t)
//  logging(t.unsafePerformSync)
//  logging(t.unsafePerformSyncAttempt)
//  t.unsafePerformAsync(logging)
//
//  val attempt: Task[Throwable \/ Int] = t.attempt
//  logging(attempt)
//
//  logging(t.get)
//
//  logging("-------------")
//
//  private def successTask: Task[Int] = for {
//    i <- Task(withSleep(1))
//    j <- Task(withSleep(2))
//    k <- Task(withSleep(3))
//  } yield {
//    i * j * k
//  }
//  private def willFail = successTask.flatMap { i => Task(withSleep(i / 0)) }
//
//  private def handler[A](a: => A): PartialFunction[Throwable, A] = { case t: Throwable => a }
//
//  logging(willFail.attempt.unsafePerformSync)
//  logging(willFail.flatMap { i => Task(withSleep(i / 0)) }.handle(handler(100)).attempt.unsafePerformSync)

  Task { Thread.sleep(100); 100 }.timed(10.millis).unsafePerformAsync(logging)
  Task.apply(100).unsafePerformSyncAttempt

  Task { 100 }.retry(List.fill(2)(10.millis)).unsafePerformAsync(logging)
  Task { sys.error("fail"); 100 }.retry(List.fill(2)(10.millis)).unsafePerformAsync(logging)

  Task.schedule(100, 10.millis).unsafePerformAsync(logging)

  Thread.sleep(1000)
}

