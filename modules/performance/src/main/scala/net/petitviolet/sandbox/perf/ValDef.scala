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

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def _val() = {
    val n = 10
    val run: Int => Int = _ * 2
    run(n)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def _def() = {
    val n = 10
    def run(i: Int): Int = i * 2
    run(n)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def _val2() = {
    val n = 10
    val run: Int => Int = _ * n
    run(n)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def _def2() = {
    val n = 10
    def run(i: Int): Int = i * n
    run(n)
  }
}
