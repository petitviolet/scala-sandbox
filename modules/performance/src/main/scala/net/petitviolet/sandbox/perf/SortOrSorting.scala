package net.petitviolet.sandbox.perf

import java.util.Comparator

import org.openjdk.jmh.annotations._

import scala.reflect.ClassTag
import scala.util.{Random, Sorting}

/**
 * [info] Benchmark                     (NUM)   Mode  Cnt       Score       Error  Units
 * [info] SortOrSorting.sortInt           100  thrpt  100  293554.545 ±  3485.854  ops/s
 * [info] SortOrSorting.sortTarget        100  thrpt  100  248718.350 ±  2176.264  ops/s
 * [info] SortOrSorting.sortingInt        100  thrpt  100  885182.653 ± 22031.750  ops/s
 * [info] SortOrSorting.sortingTarget     100  thrpt  100  237478.288 ±  2856.901  ops/s
 *
 * [info] SortOrSorting.sortInt        100000  thrpt  100      55.135 ±     0.279  ops/s
 * [info] SortOrSorting.sortTarget     100000  thrpt  100      33.935 ±     0.220  ops/s
 * [info] SortOrSorting.sortingInt     100000  thrpt  100     148.352 ±     0.950  ops/s
 * [info] SortOrSorting.sortingTarget  100000  thrpt  100      32.727 ±     0.889  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f5 .*SortOrSorting.*'
 */
@State(Scope.Benchmark)
class SortOrSorting {
  @Param(Array("100", "100000"))
  var NUM: Int = _

  var ints: Seq[Int] = _
  var targets: Seq[Target] = _

  @Setup
  def setup() = {
    ints = (1 to NUM).map { _ => Random.nextInt(NUM) }
    targets = (1 to NUM).map { _ => Target(Random.nextInt(NUM)) }
  }

  val intClassTag: ClassTag[Int] = ClassTag(classOf[Int])
  val intOrdering: Ordering[Int] = implicitly[Ordering[Int]]

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sortInt(): Seq[Int] = {
    ints.sorted(intOrdering)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sortingInt(): Seq[Int] = {
    Sorting.stableSort(ints)(intClassTag, intOrdering)//.toSeq
  }

  case class Target(value: Int)
  object TargetOrdering extends Ordering[Target] {
    override def compare(x: Target, y: Target): Int = x.value compare y.value
  }
  val targetClassTag: ClassTag[Target] = ClassTag(classOf[Target])
  val targetOrdering: Ordering[Target] = TargetOrdering

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sortTarget(): Seq[Target] = {
    targets.sorted(targetOrdering)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def sortingTarget(): Seq[Target] = {
    Sorting.stableSort(targets)(targetClassTag, targetOrdering)//.toSeq
  }

}

