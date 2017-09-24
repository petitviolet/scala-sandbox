package net.petitviolet.sandbox.z

import net.petitviolet.sandbox.Logging

import scalaz.concurrent.Future

object FuturePrac extends App with Logging {
  private def withSleep[A](a: => A): A = {
    Thread.sleep(100)
    a
  }
//
//  Future.now { withSleep(100) }.log().flatMap { i => Future.now(i * 2) }.map { _ * 3 }.unsafePerformSync.log()
//  Future.delay { withSleep(100) }.log().flatMap { i => Future.now(i * 2) }.map { _ * 3 }.unsafePerformSync.log()


  import Thread.sleep
  (for {
    i <- Future.apply(100)
    j <- Future.now(200)
    k <- Future.delay(300)
  } yield i + j + k).unsafePerformAsync(println)
  Thread.sleep(1000)

  Future.apply { sys.error("error"); 300 }.unsafePerformAsync(println)

  Thread.sleep(1000)
//  Future.delay { sys.error("error"); 300 }.unsafePerformAsync(println)
//
//  Thread.sleep(1000)
//
//  Future.now { sys.error("error"); 300 }.unsafePerformAsync(println)
//  Thread.sleep(1000)
//  Future.delay { Thread.sleep(100); 1 }.unsafePerformAsync(println)
//  Future.now { 2 }.unsafePerformAsync(println)
//  Future.apply { 3 }.unsafePerformAsync(println)
//  sleep(200)
//
//  println("start")
//
//  println(Future.delay { sys.error("error"); 100 }.unsafePerformSync)
//  println(Future.now { sys.error("error"); 100 }.unsafePerformSync)
//  println(Future.apply { sys.error("error"); 100 }.unsafePerformSync)
//
//  Thread.sleep(1000)
//  println("finish")
}
