package net.petitviolet.sandbox

import scalaz.Scalaz._
//import Scalaz._

object FuncPrac extends App {
  case class Box(id: Long, name: String)

  val f: Int => String = _.toString
  val g: Int => Long = _.toLong
  val h: String => Long => Box = name => id => Box(id, name)

  val i: Int => Box = for {
    x <- f
    y <- g
  } yield h(x)(y)

  println(i(10))
}
