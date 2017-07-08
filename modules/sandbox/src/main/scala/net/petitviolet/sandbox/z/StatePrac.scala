package net.petitviolet.sandbox.z

import scalaz.Scalaz._
import scalaz._

object StatePrac extends App {

  def sample(state: Seq[Int]) = for {
    _ <- put[Seq[Int]](Seq(1, 2, 3))
    _ <- modify[Seq[Int]] { Seq(10, 20) ++: _ }
    x <- gets[Seq[Int], Int] { _.head }
    y <- gets[Seq[Int], Int] { _.last }
  } yield x * y

  def initialize(i: Int) = StateT[Option, Int, Int] { (s: Int) =>
    if (s % 2 == 0) Some(s, s) else None
  }

  val result = sample(Seq(1, 2, 3))
  println(result)
  // (List(10, 20, 1, 2, 3),30)
}

