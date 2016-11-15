package net.petitviolet.sandbox

import scala.util.Random

object OptionMapWithNone extends App {
  implicit class RecoveryNone[A](opt: Option[A]) {
    def mapToOption[B](someF: A => B)(noneF: => Option[B]): Option[B] = {
      opt map { a => Option(someF(a)) } getOrElse noneF
    }
  }

  object IntRepository {
    def findNext: Option[Int] = {
      val r = Random.nextInt(100)
      if (r > 50) Some(r) else None
    }
  }

  {
    val target: Option[Int] = Some(100)
    val result = target.mapToOption { _ * 2 } { IntRepository.findNext }
    assert(Some(200) == result)
  }
  {
    val target: Option[Int] = None
    val result = target.mapToOption { _ * 2 } { IntRepository.findNext }
    println(result)
  }
}

object OptionMapSeq extends App {

  implicit class SeqOptionMap[A](val opts: Seq[Option[A]]) extends AnyVal {
    def x = ()
  }
}
