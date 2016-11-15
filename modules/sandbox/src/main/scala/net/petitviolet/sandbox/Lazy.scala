package net.petitviolet.sandbox

object Lazy extends App {
  private class Inner(i: Int) {
    lazy val value: Int = {
      println(s"i => $i")
      i * 1000
    }
  }

  1 to 100 foreach { i =>
    val c = new Inner(i)
    c.value
    c
  }

}
