package net.petitviolet.sandbox.perf

import java.security.SecureRandom

import org.openjdk.jmh.annotations._

import scala.concurrent.forkjoin.ThreadLocalRandom
import scala.util.Random

/**
 * ThreadLocalで良いならそっちを使う
 * [info] Benchmark                               Mode  Cnt          Score          Error  Units
 * [info] RandomOrThreadLocalRandom.normal       thrpt   10   13459094.021 ±  2992000.545  ops/s
 * [info] RandomOrThreadLocalRandom.secure       thrpt   10    1872450.765 ±    47772.059  ops/s
 * [info] RandomOrThreadLocalRandom.threadLocal  thrpt   10  168071694.715 ± 28660101.485  ops/s
 *
 * sbt 'project performance' 'jmh:run -i 10 -wi 10 -f1 -t4 .*RandomOrThreadLocalRandom.*'
 */
@State(Scope.Benchmark)
class RandomOrThreadLocalRandom {
  val NUM: Int = 10000

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def normal(): Int = {
    import scala.util.Random
    Random.nextInt(NUM)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def threadLocal(): Int = {
    ThreadLocalRandom.current().nextInt(NUM)
  }

  val secureRandom = SecureRandom.getInstanceStrong
  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def secure(): Int = {
    secureRandom.nextInt(NUM)
  }
}
