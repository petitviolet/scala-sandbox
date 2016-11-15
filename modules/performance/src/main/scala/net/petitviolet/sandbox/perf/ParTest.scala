package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

@State(Scope.Benchmark)
class ParTest {
  var mapValues: Map[Long, String] = _
  val id = 1L
  val ids: Set[Long] = (1L to 500L).toSet

  @Setup
  def setup(): Unit = {
    val size = 100000L
    mapValues = (1L to size).map { i => i -> s"value: i" }(collection.breakOut)
  }
//
//  @Benchmark
//  @BenchmarkMode(Array(Mode.Throughput))
//  def benchGet = {
//    get(id)
//  }

  def get(id: Long): Option[String] = mapValues.get(id)

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def benchBulkGet = {
    bulkGet(ids)
  }
  def bulkGet(ids: Set[Long]): Set[String] = ids.flatMap { this.get }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def benchBulkGetPar = {
    bulkGetPar(ids)
  }

  def bulkGetPar(ids: Set[Long]): Set[String] = ids.par.flatMap { this.get }.seq

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def benchBulkGetFilter = {
    bulkGetFilter(ids)
  }

  def bulkGetFilter(ids: Set[Long]): Set[String] = mapValues.collect { case (k, v) if ids.contains(k) => v }(collection.breakOut)
}

