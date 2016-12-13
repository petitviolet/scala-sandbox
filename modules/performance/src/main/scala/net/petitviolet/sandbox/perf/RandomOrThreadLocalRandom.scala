package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.util.Random

/**
 * ThreadLocalで良いならそっちを使う
 * [info] Benchmark                               Mode  Cnt          Score          Error  Units
 * [info] RandomOrThreadLocalRandom.normal       thrpt   10   15042964.039 ±  5237795.495  ops/s
 * [info] RandomOrThreadLocalRandom.threadLocal  thrpt   10  203105440.285 ± 11994288.451  ops/s
 * sbt 'project performance' 'jmh:run -i 10 -wi 10 -f1 -t4 .*RandomOrThreadLocalRandom.*'
 */
@State(Scope.Benchmark)
class RandomOrThreadLocalRandom {
  private val limit = 10000
  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def normal(): Int = {
    Random.nextInt(limit)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def threadLocal(): Int = {
    ThreadLocalRandom.current().nextInt(limit)
  }
}
