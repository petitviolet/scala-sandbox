package net.petitviolet.sandbox.z

import java.util.concurrent.Executors

import net.petitviolet.sandbox.Logging

import scala.concurrent.{ ExecutionContext, Future }
import scalaz.{ Monoid, Nondeterminism, Reducer, Scalaz, \/, \/- }
import scalaz.concurrent.Task

object TaskPrac extends App with Logging {
  //  private def withSleep[A](a: => A): A = {
  //    Thread.sleep(100)
  //    a
  //  }
  //  val a = Task.delay(withSleep(100))
  //  logging(a)
  //  logging(a.unsafePerformSync)

  implicit val ec: ExecutionContext = ExecutionContext.fromExecutorService(
    Executors.newFixedThreadPool(100)
  )

  def future(i: Int) = Future {
    println(s"future sleeping: $i")
    Thread.sleep(i * 10)
    println(s"future wakeup: $i")
    i
  }

  {
    val i = future(10)
    val j = future(20)
    val k = future(30)

    (for {
      _i <- i
      _j <- j
      _k <- k
    } yield { _i * _j * _k }).onComplete(println)

    Thread.sleep(1000)
  }

  def task(i: Int) = Task.unsafeStart {
    println(s"task sleeping: $i")
    Thread.sleep(i * 10)
    println(s"task wakeup: $i")
    i
  }

  //  {
  //    println("============")
  //    (for {
  //      i <- task(1)
  //      j <- task(2)
  //      k <- task(3)
  //    } yield {i + j + k }).unsafePerformAsync(println)
  //
  //    Thread.sleep(1000)
  //  }
  {
    println("============")

    val k = task(3)
    val i = task(1)
    val j = task(2)
    (for {
      _i <- i
      _k <- k
      _j <- j
    } yield { _i + _j + _k }).unsafePerformAsync(println)

    Thread.sleep(1000)
  }
  //  {
  //    println("============")
  //    val i = task(10)
  //    val j = task(20)
  //    val k = task(30)
  //    val reducer = {
  //      val adder = (i1: Int) => (i2: Int) => i1 * i2
  //      Reducer.apply[Int, Int](identity, adder, adder)(Scalaz.intInstance)
  //    }
  //    Task.reduceUnordered(List(k, i, j))(reducer).unsafePerformAsync(println)
  ////    Task.gatherUnordered(i :: j :: k :: Nil).map { _.foldLeft(1) { _ * _ } }.unsafePerformAsync(println)
  //    Thread.sleep(1000)
  //  }
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

  //  Task { Thread.sleep(100); 100 }.timed(10.millis).unsafePerformAsync(logging)
  //  Task.apply(100).unsafePerformSyncAttempt
  //
  //  Task { 100 }.retry(List.fill(2)(10.millis)).unsafePerformAsync(logging)
  //  Task { sys.error("fail"); 100 }.retry(List.fill(2)(10.millis)).unsafePerformAsync(logging)
  //
  //  Task.schedule(100, 10.millis).unsafePerformAsync(logging)

  Thread.sleep(1000)
  //
  //  {
  //    class Transaction(var status: String = "OPEN") {
  //      def close() = status = "CLOSED"
  //    }
  //    def withTransaction[A](f: Transaction => Task[A]): Task[A] = {
  //      val ctx = new Transaction("OPEN")
  //      try {
  //        f(ctx)
  //      } finally { ctx.close() }
  //    }
  //
  ////    val t = withTransaction { ctx =>
  ////      Task(s"transaction status: ${ctx.status}")
  ////    }
  ////    t.unsafePerformAsync(println)
  //
  //    val t = Task {
  //      withTransaction { ctx =>
  //        Task(s"transaction status: ${ctx.status }")
  //      }
  //    }
  //    t.unsafePerformAsync(println)
  //    Thread.sleep(1000)
  //  }
}

