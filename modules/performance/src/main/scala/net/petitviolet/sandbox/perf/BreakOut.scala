package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * collectionの変換にはcollection.breakOutを使う
 * [info] Benchmark           Mode  Cnt      Score      Error  Units
 * [info] BreakOut.breakOut  thrpt   10  13273.536 ±  965.511  ops/s
 * [info] BreakOut.normal    thrpt   10   9800.449 ± 1675.616  ops/s
 * sbt 'project performance' 'jmh:run -i 10 -wi 10 -f1 -t4 .*BreakOut.*'
 */
@State(Scope.Benchmark)
class BreakOut {
  val NUM = 10000
  val targets: Seq[Int] = 1 to NUM

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def normal(): Set[Int] = {
    targets.map { _ % 5 }.toSet
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def breakOut(): Set[Int] = {
    targets.map { _ % 5 }(collection.breakOut)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def normalMap(): Map[Int, String] = {
    targets.map { i => i -> s"value: $i" }.toMap
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def breakOutMap(): Map[Int, String] = {
    targets.map { i => i -> s"value: $i" }(collection.breakOut)
  }
}
