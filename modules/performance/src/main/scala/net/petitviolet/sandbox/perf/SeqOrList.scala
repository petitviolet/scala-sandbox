package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.annotation.tailrec
import scala.util.Random

/**
 * List()とSeq()はあんまり変わらない
 * [info]
 * [info] Benchmark        Mode  Cnt    Score    Error  Units
 * [info] SeqOrList.list  thrpt  100  922.159 ± 64.571  ops/s
 * [info] SeqOrList.seq   thrpt  100  938.314 ± 47.048  ops/s
 * [success] Total time: 418 s, completed 2016/12/13 8:29:30
 * sbt 'project performance' 'jmh:run -i 10 -wi 10 .*SeqOrList.*'  395.33s user 15.65s system 95% cpu 7:08.27 total
 */
@State(Scope.Benchmark)
class SeqOrList {
  val NUM = 10000
  val targets: Seq[Int] = 1 to NUM

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seq(): Seq[Seq[Int]] = {
    targets map { i => Seq(i) }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def list(): Seq[List[Int]] = {
    targets map { i => List(i) }
  }
}
