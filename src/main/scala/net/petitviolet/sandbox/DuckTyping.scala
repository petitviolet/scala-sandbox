package net.petitviolet.sandbox

import scala.language.reflectiveCalls

class DuckTyping {
  val NUM = 10000

  sealed abstract class AwesomeData(id: Int)

  case class Nice(id: Int) extends AwesomeData(id) {
    def size: Int = id * 100
    def calc(n: Int): Int = n * id
  }

  case class Great(id: Int) extends AwesomeData(id) {
    def size: Int = id * 200
    def calc(n: Int): Int = 2 * n * id
  }

  def callSizeReflection[A](a: A) = {
    val method = a.getClass.getMethod("size")
    val size = method.invoke(a)

    size.asInstanceOf[Int]
  }

  def callSizePattern[A](a: A) = {
    val size = a match {
      case nice@Nice(_) => nice.size
      case great@Great(_) => great.size
      case list@(_ : Traversable[_]) => list.size
    }
    size
  }

  type HasSize = { def size: Int }
  def callSizeDuck[A <: HasSize](hasSize: A) = {
    hasSize.size
  }



  val nice = Nice(1)
  val great = Great(2)
  val seq = Seq(1, 2, 3)

//  @Benchmark
  def benchReflection(): Unit = {
    (0 to NUM) foreach (_ => callSizeReflection(nice))
  }

//  @Benchmark
  def benchPattern(): Unit = {
    (0 to NUM) foreach (_ => callSizePattern(nice))
}

//  @Benchmark
  def benchDuck(): Unit = {
    (0 to NUM) foreach (_ => callSizeDuck(nice))
}

//
//  println(callSizeReflection(nice))
//  println(callSizePattern(nice))
//  println(callSizeDuck(nice))
//
//  println(callSizeReflection(great))
//  println(callSizePattern(great))
//  println(callSizeDuck(great))
//
//  println(callSizeReflection(seq))
//  println(callSizePattern(seq))
//  println(callSizeDuck(seq))
//
//
//    val nice = Nice(1)
//  def now = System.currentTimeMillis
//  def performance(num: Int, func: => Unit) = {
//    val start = now
//    (0 until num) foreach { _ => func }
//    val end = now
//    println(s"Time ${end - start}")
//  }
//
//  val count = 1000000
//  performance(count, callSizeReflection(nice))
//  performance(count, callSizeDuck(nice))
}
