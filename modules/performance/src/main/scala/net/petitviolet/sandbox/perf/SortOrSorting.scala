package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.util.{Random, Sorting}

/**
 * [info] Benchmark               Mode  Cnt     Score     Error  Units
 * [info] SortOrSorting.sort     thrpt   20   628.614 ±  68.174  ops/s
 * [info] SortOrSorting.sorting  thrpt   20  1802.910 ± 112.554  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*SortOrSorting.*'
 */
@State(Scope.Benchmark)
class SortOrSorting {
  private val NUM = 10000
  val target: Seq[Int] = (1 to NUM).map { _ => Random.nextInt(NUM) }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sort() = {
    target.sorted
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sorting() = {
    Sorting.stableSort(target)
  }
}
