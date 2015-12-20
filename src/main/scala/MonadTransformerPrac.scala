import scala.language.higherKinds
import scalaz._
import Scalaz._

object MonadTransformerPrac extends App {
}

object Awesome {
  def calc(a: Int, b: Int): Option[Int] =
    a * b match {
      case x if x % 2 == 0 => None
      case x => Some(x)
    }

}

object SimpleState extends App {
  def sample = for {
    _ <- init[Seq[Int]]
    _ <- modify[Seq[Int]] { Seq(10, 20) ++: _ }
    x <- gets[Seq[Int], Int] { _.head }
    y <- gets[Seq[Int], Int] { _.last }
    _ <- modify[Seq[Int]] { s =>
      Awesome.calc(x, y) match {
        case Some(n) => Seq(n) ++: s
        case None => s
      }
    }
    z <- gets[Seq[Int], Int] { _.max }
  } yield x + y + z

  val result = sample(Seq(1 ,2, 3))
  println(result)
}

trait MyOptionState[S] {

  def initS = StateT[Option, S, S] { (s: S) => Some(s, s) }

  def putS(i: S) = StateT[Option, S, Unit] { _ => Some(i, ()) }

  def modifyS(f: S => S) = initS flatMap { (s: S) =>
    StateT[Option, S, Unit] { _ => Some(f(s), ())}
  }

  def getsS[A](f: S => A) = StateT[Option, S, A] { (s: S) => Some(s, f(s)) }
}

object StateInt extends App with MyOptionState[Int] {
  def sample = for {
    i <- initS
    _ <- modifyS(s => s * 100)
    x <- getsS[Int](_ * 20)
  } yield x

  val result = sample(9)
  println(result)
}

object StateList extends App with MyOptionState[Seq[Int]] {
  def sample = for {
    i <- initS
    _ <- modifyS(s => s map { _ *100 })
    x <- getsS[Int](_.head)
  } yield x

  val result = sample(Seq(1, 2, 3))
  println(result)
}

object StateListT extends App {
//  def sample = for {
//    i <- initS
//    _ <- modifyS(s => s map { _ *100 })
//    _x = getsS[Option[Int]](_.headOption)
//    x <- _x
//  } yield x

  def sample = for {
    - <- StateT[Option, Seq[Int], String] { s => Option(s, "Start")}
    x <- StateT[Option, Seq[Int], String] { s => Option(s, "Middle") }.lift[List]
    y <- StateT[List, Seq[Int], String] { s => List((s, "End")) }
//    y <- StateT[Option, Seq[Int], String] { s => Option(s, "End") }
  } yield x + y

  val result = sample(Seq(1, 2, 3))
  println(result)
}
