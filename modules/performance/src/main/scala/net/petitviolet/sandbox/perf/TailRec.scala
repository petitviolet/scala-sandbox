package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.annotation.tailrec

/**
 * [info] Benchmark            Mode  Cnt  Score   Error  Units
 * [info] TailRec.notTailrec  thrpt   20  2.933 ± 0.683  ops/s
 * [info] TailRec.tailrec     thrpt   20  3.483 ± 0.547  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*TailRec.*'
 */
@State(Scope.Benchmark)
class TailRec {
  private val NUM = 10000
  val target: Seq[Int] = 1 to NUM

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def tailrec() = {
    @tailrec
    def makeMsg(res: String)(seq: Seq[Int]): String = {
      seq match {
        case Seq() => res
        case Seq(head, tail@_*) => makeMsg(s"$res,$head")(tail)
      }
    }

    makeMsg("")(target)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def notTailrec() = {
    def makeMsg(res: String)(seq: Seq[Int]): String = {
      seq match {
        case Seq() => res
        case Seq(head, tail@_*) => makeMsg(s"$res,$head")(tail)
      }
    }

    makeMsg("")(target)
  }
}
