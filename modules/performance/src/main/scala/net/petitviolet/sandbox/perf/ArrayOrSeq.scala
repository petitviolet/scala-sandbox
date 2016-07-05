package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._
import scala.annotation.tailrec

@State(Scope.Benchmark)
class ArrayOrSeq {
  val NUM = 10000
  val arrayTarget = (0 to NUM).toArray
  val seqTarget = (0 to NUM).toSeq

  @Benchmark
  def seqSep() = {
    @tailrec
    def makeMsg(res: String)(seq: Seq[Int]): String = {
      seq match {
        case Seq() => res
        case Seq(head, tail @ _*) => makeMsg(s"$res,$head")(tail)
      }
    }
    makeMsg("")(seqTarget)
  }

  @Benchmark
  def signSep() = {
    @tailrec
    def makeMsg(res: String)(seq: Seq[Int]): String = {
      seq match {
        case Seq() => res
        case head +: tail => makeMsg(s"$res,$head")(tail)
      }
    }
    makeMsg("")(seqTarget)
  }

  @Benchmark
  def listSep() = {
    @tailrec
    def makeMsg(res: String)(seq: List[Int]): String = {
      seq match {
        case Seq() => res
        case head :: tail => makeMsg(s"$res,$head")(tail)
      }
    }
    makeMsg("")(seqTarget.toList)
  }

  @Benchmark
  def vecSep() = {
    @tailrec
    def makeMsg(res: String)(seq: Vector[Int]): String = {
      seq match {
        case Seq() => res
        case head +: tail => makeMsg(s"$res,$head")(tail)
      }
    }
    makeMsg("")(seqTarget.toVector)
  }
}
