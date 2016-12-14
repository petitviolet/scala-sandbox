package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._
import scala.annotation.tailrec

/**
  * [info] Benchmark            Mode  Cnt  Score   Error  Units
  * [info] ArrayOrSeq.listSep  thrpt   10  5.201 ± 1.135  ops/s
  * [info] ArrayOrSeq.seqSep   thrpt   10  5.625 ± 0.662  ops/s
  * [info] ArrayOrSeq.signSep  thrpt   10  6.354 ± 0.328  ops/s
  * [info] ArrayOrSeq.vecSep   thrpt   10  6.552 ± 0.267  ops/s
  * sbt 'project performance' 'jmh:run -i 10 -wi 10 -f1 -t4'  543.38s user 10.36s system 332% cpu 2:46.54 total
  */
@State(Scope.Benchmark)
class ArrayOrSeq {
  val NUM = 10000
  val arrayTarget = (0 to NUM).toArray
  val seqTarget = (0 to NUM).toSeq

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
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
  @BenchmarkMode(Array(Mode.Throughput))
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
  @BenchmarkMode(Array(Mode.Throughput))
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
  @BenchmarkMode(Array(Mode.Throughput))
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
