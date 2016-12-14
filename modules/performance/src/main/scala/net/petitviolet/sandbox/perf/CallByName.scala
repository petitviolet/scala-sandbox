package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * [info] Benchmark          Mode  Cnt           Score           Error  Units
 * [info] CallByName.name   thrpt   20  2832880888.965 ± 175901562.751  ops/s
 * [info] CallByName.value  thrpt   20   451953102.136 ±  18617394.279  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*CallByName.*'
 */
@State(Scope.Benchmark)
class CallByName {

  def byNameLogging(value: => String) = ()

  def byValueLogging(value: String) = ()

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def name() = {
    byNameLogging("value")
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def value() = {
    byValueLogging("value")
  }

}
