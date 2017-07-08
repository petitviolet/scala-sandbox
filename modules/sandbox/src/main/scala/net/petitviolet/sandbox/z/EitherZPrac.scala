package net.petitviolet.sandbox.z

import scalaz.Scalaz._
import scalaz._

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
    type MyError[A] = String \/ A
    //  type OptEither[A] = OptionT[{ type λ[I] = \/[String, I] }#λ, A]
    type OptEither[A] = OptionT[MyError, A]

    val a: OptEither[Int] = 100.point[OptEither]
    // val a: OptEither[Int] = scalaz.Scalaz.ApplicativeIdV[Int](100).point[OptEither](scalaz.this.OptionT.optionTMonadPlus[MyError](scalaz.this.\/.DisjunctionInstances1[String]));
    val b: OptEither[Int] = OptionT("This is error".left: MyError[Option[Int]])

    val x = for {
      i <- a
      j <- b
    } yield i |+| j
    //    val x: scalaz.OptionT[MyError,Int] = a.map[Int](((i: Int) => i))(scalaz.this.\/.DisjunctionInstances1[String]);

    println("--------------")
    println(a)
    println(a.run)
    println(b)
    println(b.run)
    println(x)
    println(x.run)
  }

  {
    println("--------------")
    type MyError[A] = Throwable \/ A
    type OptEither[A] = OptionT[MyError, A]
    case class User(id: Long)
    case class Activity(id: Long, userId: Long)
    case class NoUser(msg: String) extends Exception(msg)
    case class NoActivity(msg: String) extends Exception(msg)

    def findById(id: Long): Throwable \/ User = {
      if (id % 2 == 0) {
        User(id).right
      } else NoUser("No User").left
    }

    def findActivity(userId: Long): Throwable \/ Activity = {
      if (userId % 4 == 0) {
        Activity(userId / 4L, userId).right
      } else NoActivity("No Activity").left
    }

    println((OptionT(some(100L).point[MyError]) flatMapF findById).run)
    println((OptionT(some(101L).point[MyError]) flatMapF findById).run)
    println((OptionT(none.point[MyError]) flatMapF findById).run)

    println((OptionT(some(100L).point[MyError]) flatMapF findActivity).run)
    println((OptionT(some(102L).point[MyError]) flatMapF findActivity).run)

    val result = for {
      userId <- OptionT(some(100L).point[MyError])
      user = findById(userId)
    } yield user
    println(result)

  }
}
