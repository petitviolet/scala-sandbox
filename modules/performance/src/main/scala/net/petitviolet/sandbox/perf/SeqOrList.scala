package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.util.Random

/**
 * [info] Benchmark                     Mode  Cnt           Score         Error  Units
 * [info] SeqOrList.listApply          thrpt  100   11780452.145 ±  123376.558  ops/s
 * [info] SeqOrList.listColon          thrpt  100  109701449.482 ± 4825541.777  ops/s
 * [info] SeqOrList.seq                thrpt  100   13267611.620 ±  197315.652  ops/s
 * [info] SeqOrList.vector             thrpt  100   12520556.750 ±   38746.670  ops/s
 * [info] SeqOrList.listApplyMultiple  thrpt  100    9960213.766 ±   47018.006  ops/s
 * [info] SeqOrList.listColonMultiple  thrpt  100   49911927.472 ± 3193454.950  ops/s
 * [info] SeqOrList.seqMultiple        thrpt  100    7336909.678 ±  663634.817  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f5 .*SeqOrList.*'
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
  }


  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seqMultiple(): Seq[Int] = {
    Seq.apply(NUM, NUM, NUM)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def listApplyMultiple(): Seq[Int] = {
    List.apply(NUM, NUM, NUM)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def listColonMultiple(): Seq[Int] = {
    NUM :: NUM :: NUM :: Nil
  }

//  @Benchmark
//  @BenchmarkMode(Array(Mode.Throughput))
//  def vector(): Seq[Int] = {
//    Vector.apply(1)
//  }

}
