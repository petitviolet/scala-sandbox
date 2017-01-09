package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.collection.mutable.ListBuffer
import scala.util.Random

/**
 * [info] Benchmark             Mode  Cnt          Score         Error  Units
 * [info] SeqOrList.listApply  thrpt  200   11780452.145 ±  123376.558  ops/s
 * [info] SeqOrList.listColon  thrpt  200  109701449.482 ± 4825541.777  ops/s
 * [info] SeqOrList.seq        thrpt  200   13267611.620 ±  197315.652  ops/s
 * [info] SeqOrList.vector     thrpt  200   12520556.750 ±   38746.670  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 .*SeqOrList.*'
 */
@State(Scope.Benchmark)
class SeqOrList {
  var NUM: Int = _
  val targets: Seq[Int] = 1 to NUM

  @Setup
  def setup() = NUM = Random.nextInt(1000)

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seq(): Seq[Int] = {
    Seq.apply(NUM)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def listApply(): Seq[Int] = {
    List.apply(NUM)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def listColon(): Seq[Int] = {
    NUM :: Nil
    new ListBuffer
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def vector(): Seq[Int] = {
    Vector.apply(1)
  }
}
