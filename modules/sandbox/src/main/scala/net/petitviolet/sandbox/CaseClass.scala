package net.petitviolet.sandbox

object CaseClass extends App {
  private sealed abstract class Point(x: Int, y: Int)
  private case class ColorPoint(x: Int, y: Int, color: Int) extends Point(x, y)

  println (ColorPoint(1, 1, 1) == ColorPoint(1, 1, 1))
}
