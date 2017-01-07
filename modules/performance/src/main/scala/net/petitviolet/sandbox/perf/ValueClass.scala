package net.petitviolet.sandbox.perf

import java.util.UUID

import org.openjdk.jmh.annotations._

import scala.util.Random

/**
 * [info] Benchmark                   Mode  Cnt          Score          Error  Units
 * [info] ValueClass.implicitNormal  thrpt  200  372242301.783 ± 11045544.159  ops/s
 * [info] ValueClass.implicitValue   thrpt  200  365348990.992 ±  6545855.156  ops/s
 * [info] ValueClass.normal          thrpt  200   61563331.573 ±  2285286.937  ops/s
 * [info] ValueClass.raw             thrpt  200  153345616.699 ±  4431754.291  ops/s
 * [info] ValueClass.value           thrpt  200  154246211.102 ±  3985144.208  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 .*ValueClass.*'
 */
@State(Scope.Benchmark)
class ValueClass {
  val n: Int =new Random().nextInt(100)
  val id: Long = new Random().nextInt(100).toLong
  val name = UUID.randomUUID().toString

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def raw(): Raw.UserRaw = {
    Raw.UserRaw(id, name)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def normal(): Normal.User = {
    Normal.User(Normal.Id(id), Normal.Name(name))
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def value(): Value.UserValue = {
    Value.UserValue(Value.IdValue(id), Value.NameValue(name))
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def implicitNormal(): Int = {
    import Implicits.SuperInt
    n.double
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def implicitValue(): Int = {
    import Implicits.HyperInt
    n.triple
  }

}

object Raw {
  case class UserRaw(id: Long, name: String)
}

object Normal {
  case class User(id: Id, name: Name)
  case class Id(value: Long)
  case class Name(value: String)
}

object Value {
  // not Value class
  case class UserValue(id: IdValue, name: NameValue)
  case class IdValue(value: Long) extends AnyVal
  case class NameValue(value: String) extends AnyVal
}

object Hoge {
  Normal.User(Normal.Id(1L), Normal.Name("hoge"))
  Value.UserValue(Value.IdValue(1L), Value.NameValue("hoge"))
}

object Implicits {
  implicit class AwesomeInt(val n: Int) extends AnyVal {
    def double = n * 2
  }
  implicit class SuperInt(val n: Int) {
    def double = n * 2
  }

  implicit class HyperInt(val n: Int) extends AnyVal {
    def triple = n * 3
  }
}

private object ValueExample {
  class Awesome(val value: Int) extends AnyVal {
    def double = value * 2
  }
}
