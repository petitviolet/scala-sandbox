package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * [info] Benchmark             Mode  Cnt         Score        Error  Units
 * [info] Casting.collectIf    thrpt   80  18748700.908 ± 539914.170  ops/s
 * [info] Casting.collectType  thrpt   80  19270658.732 ± 489737.141  ops/s
 * [info] Casting.filterMap    thrpt   80   6665927.072 ± 477516.616  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f4 .*Casting.*'
 */
@State(Scope.Benchmark)
class Casting {
  val target: Seq[Any] = List(1, "2", 3.0, 4L, 5.0f)

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def filterMap(): Seq[Long] = {
    target.filter { _.isInstanceOf[Long] }.map { _.asInstanceOf[Long] }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def collectIf(): Seq[Long] = {
    target.collect { case x if x.isInstanceOf[Long] => x.asInstanceOf[Long] }
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def collectType(): Seq[Long] = {
    target.collect { case x: Long  => x }
  }
}
