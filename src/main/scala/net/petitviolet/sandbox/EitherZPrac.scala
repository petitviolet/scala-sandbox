package net.petitviolet.sandbox

import scalaz._, Scalaz._

object EitherZPrac extends App {
  {
    val x = for {
      a <- "success".right
      b <- 1.left[Int]
      c <- "nice".right
    } yield a + b + c
    println(x)
  }


  {
    val x = "hogehoge".left[Int] >>= { x => (x + 1).right }
    println(x)
    val y = x | "nice"
    println(y)
  }

  {
    type A = String \/ Option[Int]
    val a: A = Option(10).right
    val b: A = Option(20).right
    val c: A = "No Value".left

    val x = for {
      i <- a
      j <- b
      k <- c
    } yield k
    println(x)
  }

  {
    val x = for {
      i <- Option(10) \/> "エラーだよ"
      j <- Option(20) \/> "だめだよ"
    } yield i * j
    println(x)
  }

  {
    type Error[A] = String \/ A
    //  type OptEither[A] = OptionT[{ type λ[I] = \/[String, I] }#λ, A]
    type OptEither[A] = OptionT[Error, A]

    val a: OptEither[Int] = 100.point[OptEither]
    // val a: OptEither[Int] = scalaz.Scalaz.ApplicativeIdV[Int](100).point[OptEither](scalaz.this.OptionT.optionTMonadPlus[Error](scalaz.this.\/.DisjunctionInstances1[String]));

    val x = for {
      i <- a
    } yield i
//    val x: scalaz.OptionT[Error,Int] = a.map[Int](((i: Int) => i))(scalaz.this.\/.DisjunctionInstances1[String]);

    println(x)
  }
}
