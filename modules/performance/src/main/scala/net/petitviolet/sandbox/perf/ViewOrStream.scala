package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.collection.SeqView
import scala.language.postfixOps
import scala.util.{Random, Sorting}

/**
 * 遅延評価させつつ、ならviewで良さそう
 * [info] Benchmark             Mode  Cnt      Score      Error  Units
 * [info] ViewOrStream.seq     thrpt   20  12331.039 ±  214.061  ops/s
 * [info] ViewOrStream.stream  thrpt   20   1453.502 ±  469.288  ops/s
 * [info] ViewOrStream.view    thrpt   20   5978.444 ± 1287.059  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*ViewOrStream.*'
 */
@State(Scope.Benchmark)
class ViewOrStream {
  private val NUM = 10000
  val target: Seq[Int] = (1 to NUM).map { _ => Random.nextInt(NUM) }
  val targetView: SeqView[Int, Seq[Int]] = target.view
  val targetStream: Stream[Int] = target.toStream

  private val f: Int => Int = _ + 1

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def seq(): Seq[Int] = {
    target map f
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def view(): Seq[Int] = {
    targetView.map(f).force
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def stream():Seq[Int] = {
    targetStream.map(f).force
  }
}
