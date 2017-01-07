package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.reflect.ClassTag
import scala.util.{Random, Sorting}

/**
 * [info] Benchmark               (NUM)   Mode  Cnt       Score       Error  Units
 * [info] SortOrSorting.sort        100  thrpt   20  267180.835 ± 10617.731  ops/s
 * [info] SortOrSorting.sort     100000  thrpt   20      43.220 ±     3.189  ops/s
 * [info] SortOrSorting.sorting     100  thrpt   20  706745.994 ± 37814.493  ops/s
 * [info] SortOrSorting.sorting  100000  thrpt   20     137.953 ±    14.081  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*SortOrSorting.*'
 */
@State(Scope.Benchmark)
class SortOrSorting {
  @Param(Array("100", "100000"))
  var NUM: Int = _

  var target: Seq[Int] = _

  @Setup
  def setup() = {
    target = (1 to NUM).map { _ => Random.nextInt(NUM) }
  }

  val classTag: ClassTag[Int] = ClassTag(classOf[Int])
  // default `Ordering` instance
  val ordering: Ordering[Int] = implicitly[Ordering[Int]]

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sort() = {
    target.sorted(ordering)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sorting() = {
    Sorting.stableSort(target)(classTag, ordering)
  }
}
