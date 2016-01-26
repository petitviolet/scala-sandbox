package net.petitviolet.sandbox.z

import scalaz._, Scalaz._

object OptionZ extends App {
  private case class Score[+A](value: A)

  implicit private val userMonoidLong = new Monoid[Score[Long]] {
    override def zero: Score[Long] = Score(0L)
    override def append(f1: Score[Long], f2: => Score[Long]): Score[Long] = Score(f1.value + f2.value)
  }

  implicit private val userMonoidDouble = new Monoid[Score[Double]] {
    override def zero: Score[Double] = Score(0.0)
    override def append(f1: Score[Double], f2: => Score[Double]): Score[Double] = Score(f1.value + f2.value)
  }

  implicit private val userApplicative = new Applicative[Score] {
    override def point[A](a: => A): Score[A] = Score(a)
    override def ap[A, B](fa: => Score[A])(f: => Score[(A) => B]): Score[B] = Score(f.value(fa.value))
  }

  implicit private val userMonad = new Monad[Score] { self =>
    override def point[A](a: => A): Score[A] = Score(a)
    override def bind[A, B](fa: Score[A])(f: (A) => Score[B]): Score[B] = f(fa.value)
  }

  private def option(n: Int) = {
    print("option - ")
    for {
      i <- some(Score(1L))
      j <- some(Score(2L))
      x <- if (n % 2 == 0) some(Score(3L)) else none
    } yield i |+| j |+| x
  }

  private def maybe(n: Int) = {
    import Maybe._
    print("maybe - ")
    for {
      i <- just(Score(1.0))
      j <- just(Score(2.0))
      k = just(Score(5.0)).orZero
      x <- if (n % 2 == 0) just(Score(3.0)) else empty
    } yield i |+| j |+| x |+| k
  }

  private def bind() = {
    print("bind - ")
    for {
      i <- Score(10)
      j <- Score(20)
    } yield i + j
  }

  println(option(1))
  println(option(2))

  println(maybe(1))
  println(maybe(2))

  println(bind())
}
