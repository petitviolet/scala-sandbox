package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.collection.immutable.IndexedSeq
import scala.util.Random

@State(Scope.Benchmark)
class FilterOrGroup {

  case class Target(id: Int, name: String, parentId: Int)
  val NUM = 100000
  val r = new Random()
  def parentId = r.nextInt(10)

  val targets: Seq[Target] = (0 to NUM).map { id => Target(id, s"target-$id", parentId) }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def filtering() = {
    val result: IndexedSeq[Seq[Target]] = (0 to 3) map { n =>
      val targetParentId = parentId
      targets.filter { _.parentId != targetParentId }
    }
    result.flatten.size
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def groupBying() = {
    val grouped = targets.groupBy(_.parentId)
    val result: IndexedSeq[Map[Int, Seq[Target]]] = (0 to 3) map { n =>
      val targetParentId = parentId
      grouped - targetParentId
    }
    result.flatMap(_.values).flatten.size
  }

}
