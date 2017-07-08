package net.petitviolet.sandbox

import shapeless._

sealed trait ShapelessPrac {
  object add extends Poly2 {
    implicit val i: Case[Int, Int] {
      type Result = Int
    } = at[Int, Int] {_ + _}
    implicit val l = at[Long, Long] {_ + _}
    implicit val il = at[Int, Long] {_ + _}
  }
  object asInt extends Poly1 {
    implicit val intCase = at[Int] { x => x }
    implicit val longCase = at[Long] { x => x.toInt }

    implicit def default[T] = at[T] { _ => 0 }
  }
  // 全てに対して`asInt`で変換して`add`する
  def sum[T](t: T)(implicit e: Everything[asInt.type, add.type, T]) = e(t)

  object increment extends Poly1 {
    implicit val i = at[Int] { _ + 1 }
    implicit val l = at[Long] { _ + 1 }
  }
  object decrement extends Poly1 {
    implicit val i = at[Int] { _ - 1 }
    implicit val l = at[Long] { _ - 1 }
  }
}

object ShapelessPracApp extends App with ShapelessPrac {
  assert {sum((1, 2)) == 3}

  case class Bar(s: String, i: Int)
  case class Foo(i: Int, s: String)
  assert {sum((Bar("a", 2), Bar("b", 3))) == 5}
  assert {sum((Foo(1, "a"), Foo(2, "b"))) == 3}
  assert {sum((Bar("a", 1), Foo(2, "b"))) == 3}

  case class Hoge(i1: Int, i2: Long)
  assert {sum(Hoge(1, 2L) :: Hoge(3, 4L) :: HNil) == 10}

  assert { increment(1) == 2 }
//  assert { increment("") == 1 } // cannot compile
  assert { (increment compose decrement)(1) == 1 }
}
