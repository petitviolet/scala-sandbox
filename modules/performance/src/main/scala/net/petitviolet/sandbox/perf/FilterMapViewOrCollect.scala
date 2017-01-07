package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.collection.SeqView
import scala.language.postfixOps
import scala.util.Random

/**
 * [info] Benchmark               Mode  Cnt     Score     Error  Units
 * [info] SortOrSorting.sort     thrpt   20   628.614 ±  68.174  ops/s
 * [info] SortOrSorting.sorting  thrpt   20  1802.910 ± 112.554  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*SortOrSorting.*'
 */
@State(Scope.Benchmark)
class FilterMapViewOrCollect {
  private val NUM = 10000
  val target: Seq[Int] = (1 to NUM).map { _ => Random.nextInt(NUM) }

  private val f: Int => Boolean = _ % 2 == 0
  private val g: Int => Int = _ + 1

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def filterMap(): Seq[Int] = {
    target filter f map g
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def filterMapView(): Seq[Int] = {
    target.view filter f map g force
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def collect():Seq[Int] = {
    target collect { case i if f(i) => g(i) }
  }
}
