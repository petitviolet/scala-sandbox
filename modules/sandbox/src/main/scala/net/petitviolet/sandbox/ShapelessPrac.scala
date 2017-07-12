package net.petitviolet.sandbox

import shapeless._

sealed trait ShapelessPrac {

  object toInt extends Poly1 {
    private def logging[A](a: A): A = { println(s"[toInt]$a"); a }
    implicit val intCase = at[Int] { logging }
    implicit val stringCase = at[String] { logging(_).length }
    implicit val longCase = at[Long] { l => logging(l.toInt * 100) }

    implicit def fallback[T] = at[T] { x => logging(s"default: $x"); x }
  }

  object union extends Poly2 {
    implicit val ii = at[Int, Int] { (i1, i2) => println(s"[union]$i1 + $i2"); i1 + i2 }
    implicit val is = at[Int, String] { (i, s) => val x = s"$i, $s"; println(x); x }
    implicit val il = at[Int, Long] { (i, l) => println(s"[union]$i * $l"); i * l }
  }
  // 全てに対して`asInt`で変換して`add`する
  def fold[T](t: T)(implicit e: Everything[toInt.type, union.type, T]) = e(t)

  object increment extends Poly1 {
    private def logging[A](a: A): A = { println(s"[increment]$a"); a }
    implicit val i = at[Int] { logging(_) + 1 }
    implicit val l = at[Long] { logging(_) * 100 }
    implicit val s = at[String] { logging(_) + "!" }

    implicit def fallback[A] = at[A] { x => logging(s"default: $x"); x }
  }
  object add extends Poly2 {
    implicit val ii = at[Int, Int] { _ + _ }
  }
  object show extends Poly1 {
    implicit val i = at[Int] { i => s"Int($i)" }
    implicit val l = at[Long] { l => s"Long($l)" }

    implicit def fallback[A] = at[A] { a => s"Unknown($a)" }
  }

}

object ShapelessPracApp extends App with ShapelessPrac {
  assert { add(1, 2) == 3 }
  case class Foo(i: Int, s: String)
  case class Baz(l: Long, i: Int)
  private def assert(b: => Boolean): Unit = {
    println("[START]------------------------")
    Predef.assert(b)
    println("[END]--------------------------")
  }

  //  assert { everything(toInt)(union) { (Foo(1, "hoge"), Baz(2L, 3)) } == 208 }
  assert { everywhere(increment) { (Foo(1, "hoge"), Baz(2L, 3)) } == (Foo(2, "hoge!"), Baz(200L, 4)) }
  assert { everywhere(toInt) { (Foo(1, "hoge"), Baz(2L, 3)) } == (Foo(1, "hoge"), Baz(2L, 3)) }
  //  println { everything(toInt)(union) { (Foo(1, "hoge"), Baz(2L, 3)) } }
  println { everywhere(increment) { (Foo(1, "hoge"), Baz(2L, 3)) } }
  println { everywhere(toInt) { (Foo(1, "hoge"), Baz(2L, 3)) } }
  //  assert { fold((1, 2)) == 3 }
  //
  //  case class Bar(s: String, i: Int)
  //  assert { fold((Bar("a", 2), Bar("b", 3))) == 7 }
  //  assert { fold((Foo(1, "a"), Foo(2, "b"))) == 5 }
  //  assert { fold((Bar("a", 1), Foo(2, "b"))) == 5 }
  //
  //  case class Hoge(i1: Int, i2: Long)
  //  assert { fold(Hoge(1, 2L) :: Hoge(3, 4L) :: HNil) == 10 }
  //  assert { everywhere(increment)(Hoge(1, 2L)) == Hoge(2, 3L) }
  //  assert { everything(toInt)(union)(Hoge(1, 2L)) == 3 }
  //
  //  assert { increment(1) == 2 }
  //  assert { increment(1L) == 100L }
  //  assert { increment("hoge") == "hoge!" }
  //  //  //  assert { increment(1.0f) == 2.0f } // cannot compile
  //  assert { (show compose increment)(1) == s"Int(2)" }
  //  assert { (increment andThen show)("hello") == s"Unknown(hello!)" }
}
