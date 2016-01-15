package net.petitviolet.sandbox

object ImplicitClassPerformance extends App {
  def now = System.currentTimeMillis

  val N = 100000
  def measure(func: Int => Int) = {
    val start = now
    var sum = 0
    (0 until N) foreach { i =>
      sum += func(i)
    }
    println(s"sum: $sum")
    val end = now
    end - start
  }

  implicit class ImpFoo(val foo: Foo) extends AnyVal {
    def show = foo.num
  }
  implicit class ImpHoge(val hoge: Hoge) {
    def show = hoge.num
  }

  val h = measure { i =>
    val hoge = Hoge(i)
    hoge.show
  }

  val f = measure { i =>
    val foo = Foo(i)
    foo.show
  }
  println(f)
  println(h)

}

case class Foo(num: Int)

case class Hoge(num: Int)
