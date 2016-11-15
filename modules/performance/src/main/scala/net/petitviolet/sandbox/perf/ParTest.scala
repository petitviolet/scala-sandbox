package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
class ParTest {
  var mapValues: Map[Long, String] = _

  @Setup
  def setup(): Unit = {
    val size = 100000L
    mapValues = (1L to size).map { i => i -> s"value: i" }(collection.breakOut)
  }

  @Benchmark
  def get(id: Long): Option[String] = mapValues.get(id)

  @Benchmark
  def bulkGet(ids: Set[Long]): Set[String] = ids.flatMap { this.get }

  @Benchmark
  def bulkGetPar(ids: Set[Long]): Set[String] = ids.par.flatMap { this.get }.seq
}

