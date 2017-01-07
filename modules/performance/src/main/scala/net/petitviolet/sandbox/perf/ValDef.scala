package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * valとdefでほぼ変わらず
 * [info] Benchmark      Mode  Cnt          Score          Error  Units
 * [info] ValDef._def   thrpt  200  411956332.616 ±  6515261.167  ops/s
 * [info] ValDef._def2  thrpt  200  420121145.175 ±  1775055.484  ops/s
 * [info] ValDef._val   thrpt  200  417445058.704 ±  2016498.024  ops/s
 * [info] ValDef._val2  thrpt  200  391308496.556 ± 18399874.385  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20  .*ValDef.*'
 */
@State(Scope.Benchmark)
class ValDef {
  @Param(Array("10", "100", "1000"))
  var N: Int = _

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def valLight() = {
    val run: Int => Int = _ * 2
    run(N)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def defLight() = {
    def run(i: Int): Int = i * 2
    run(N)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def valHeavy() = {
    val run: Int => Int = i => {
      (0 to i).map {_ % 5}.distinct.sum
    }

    run(N)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def defHeavy() = {
    def run(i: Int): Int = {
      (0 to i).map {_ % 5}.distinct.sum
    }

    run(N)
  }
}
