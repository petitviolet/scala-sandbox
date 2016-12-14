package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * Seq.applyの方がはやい！
 * [info] Benchmark        Mode  Cnt         Score        Error  Units
 * [info] SeqOrList.list  thrpt  200  11485516.282 ± 346706.969  ops/s
 * [info] SeqOrList.seq   thrpt  200  12462881.306 ± 545318.664  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 .*SeqOrList.*'
 */
@State(Scope.Benchmark)
class SeqOrList {
  val NUM = 10000
  val targets: Seq[Int] = 1 to NUM

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seq(): Seq[Int] = {
    Seq.apply(1)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def list(): List[Int] = {
    List.apply(1)
  }
}
