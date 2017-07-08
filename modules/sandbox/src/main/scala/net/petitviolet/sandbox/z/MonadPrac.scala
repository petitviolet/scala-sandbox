package net.petitviolet.sandbox.z

import scala.language.higherKinds
import scalaz._, Scalaz._

object MonadPrac extends App {
  private case class Target[F[_], A](attr: F[A])
  private type OptionTarget[A] = Target[Option, A]

  //  private implicit val TargetMonad = new MonadPlus[OptionTarget] {
  //
  //    override def point[A](a: => A): OptionTarget[A] = Target(some(a))
  //
  //    override def bind[A, B](fa: OptionTarget[A])(f: (A) => OptionTarget[B]): OptionTarget[B] =
  //      fa.attr.map(f) getOrElse Target(none)
  //
  //    override def empty[A]: OptionTarget[A] = Target(none)
  //
  //    override def plus[A](a: OptionTarget[A], b: => OptionTarget[A]): OptionTarget[A] = {
  //      val value = for {
  //        attrA <- a.attr
  //        attrB <- b.attr
  //      } yield attrA
  //      Target(value)
  //    }
  //  }
  //
  //  import scala.util.Random
  //  val r = new Random()
  //  def !!! = r.nextInt(3)
  //  private val fightAndWin = 0
  //  private val fightAndLose = 1
  //  private val noFight = 2
  //  def prise = r.nextInt(100)
  //
  //  sealed trait Fight {
  //    val prise: Int
  //  }
  //  private case class Prise(prise: Int) extends Fight
  //  private case object NoFight extends Fight {
  //    override val prise: Int = 0
  //  }
  //
  //  private def fight: OptionTarget[Fight] = {
  //    val result: OptionTarget[Fight] = !!! match {
  //      case `fightAndWin` => Target(some(Prise(prise)))
  //      case `fightAndLose` => Target(some(Prise(0)))
  //      case `noFight` => Target(some(NoFight))
  //      case _ => Target(none)
  //    }
  //    println(result)
  //    result
  //  }
  //
  //  implicit val fightMonoid = new Monoid[Fight] {
  //    override def zero: Fight = Prise(0)
  //
  //    override def append(f1: Fight, f2: => Fight): Fight = (f1, f2) match {
  //      case (NoFight, NoFight) => NoFight
  //      case _ => Prise(f1.prise + f2.prise)
  //    }
  //  }
  //
  //  val result = for {
  //    i <- fight
  //    j <- fight
  //    k <- fight
  //    l <- fight
  //    m <- fight
  //  } yield i |+| j |+| k |+| l |+| m
  //
  //  println("")
  //  println(result)

}
