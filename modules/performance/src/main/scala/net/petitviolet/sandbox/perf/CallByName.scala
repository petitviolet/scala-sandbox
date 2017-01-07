package net.petitviolet.sandbox.perf

import java.util.logging
import java.util.logging.Logger

import org.openjdk.jmh.annotations._
import sun.util.logging.PlatformLogger

/**
 * [info] Benchmark         (flag)   Mode  Cnt          Score         Error  Units
 * [info] CallByName.name     true  thrpt   20   10330166.283 ± 2255057.190  ops/s
 * [info] CallByName.name    false  thrpt   20  308130880.311 ± 5042012.356  ops/s
 * [info] CallByName.value    true  thrpt   20   11677715.110 ±  269180.061  ops/s
 * [info] CallByName.value   false  thrpt   20    9780137.939 ± 1191633.911  ops/s
 * sbt 'project performance' 'jmh:run -i 20 -wi 20 -f1 .*CallByName.*'
 */
@State(Scope.Benchmark)
class CallByName {

  def doSomething[T](arg: => T) = ???

  @Param(Array("true", "false"))
  var flag: Boolean = _

  private val EMPTY = ""

  def byName(value: => String, flag: Boolean): String =
    if (flag) value else EMPTY

  def byValue(value: String, flag: Boolean): String =
    if (flag) value else EMPTY

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def name() = {
    byName(s"value: $flag", flag)
  }

  @Benchmark
  @BenchmarkMode(Array(Mode.Throughput))
  def value() = {
    byValue(s"value: $flag", flag)
  }

}

/**
 * example to use call-by-name effectively
 */
private object LoggerExample {
  class Logger {
    def info(msg: => String) = ()

    def debug(msg: => String) = ()
  }

  // just example
  class MyLogger(logger: Logger, isDebugEnabled: Boolean) {
    def debug(msg: => String) =
      if (isDebugEnabled) logger.debug(msg)

    def info(msg: => String) = logger.info(msg)
  }
}
