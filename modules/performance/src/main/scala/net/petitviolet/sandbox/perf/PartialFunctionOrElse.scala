package net.petitviolet.sandbox.perf

import org.openjdk.jmh.annotations._

/**
 * [[PartialFunction.orElse]]遅い
 * [info] Benchmark                             Mode  Cnt         Score          Error  Units
 * [info] PartialFunctionOrElse.orElseJustOnc  thrpt   20  57165200.425 ±   708379.699  ops/s
 * [info] PartialFunctionOrElse.orElsePerCall  thrpt   20  32565101.727 ± 15132221.441  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 -t 1 .*PartialFunctionOrElse.*'
 */
@State(Scope.Benchmark)
class PartialFunctionOrElse {
  private val NUM = 1000
  private val sets: Set[Int] = (1 to NUM).toSet
  private var orElseDef = Def(sets)
  private var orElseVal = Val(sets)

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def orElsePerCall() = {
    orElseDef.isOk(25)
  }
  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def orElseJustOnc() = {
    orElseVal.isOk(25)
  }

}

private case class Def(sets: Set[Int]) {
  private val pf1: PartialFunction[Int, Boolean] = {
    case i if i > 100 => true
  }

  private val pf2: PartialFunction[Int, Boolean] = {
    case i if i > 50 => true
  }

  private val elsePf: PartialFunction[Int, Boolean] = {
    case _ => false
  }

  def isOk(i: Int): Boolean = {
    (pf1 orElse pf2 orElse elsePf)(i)
  }
}

private case class Val(sets: Set[Int]) {
  private val pf1: PartialFunction[Int, Boolean] = {
    case i if i > 100 => true
  }

  private val pf2: PartialFunction[Int, Boolean] = {
    case i if i > 50 => true
  }

  private val elsePf: PartialFunction[Int, Boolean] = {
    case _ => false
  }

  private val pf = pf1 orElse pf2 orElse elsePf

  def isOk(i: Int): Boolean = {
    pf(i)
  }
}
