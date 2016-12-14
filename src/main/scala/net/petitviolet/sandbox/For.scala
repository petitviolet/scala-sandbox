package net.petitviolet.sandbox

object For extends App {
  val list = Seq(1, 2, 3)
  val iOpt = Option(10)
  val jOpt = Option(20)

  val result = list.flatMap { case item =>
    iOpt.flatMap { case i =>
      jOpt.map { case j =>
        item * i * j }
    }
  }
  println(result)
}
