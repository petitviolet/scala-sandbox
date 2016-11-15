package net.petitviolet.sandbox

import java.util.concurrent.ForkJoinPool

import scala.util._
import scala.concurrent._
import scala.concurrent.duration.Duration

object FutureTest extends App {
  private implicit val ec = ExecutionContext.fromExecutor(new ForkJoinPool(16))

  {
    val start1 = System.currentTimeMillis()
    val future1 = Future { Thread.sleep(1000); 10 }
    val future2 = Future { Thread.sleep(1000); 20 }

    val f = for {
      r1 <- future1
      r2 <- future2
    } yield {
      val end = System.currentTimeMillis()
      println(s"first - result: ${r1 + r2 }, ${end - start1 }")
    }

    Await.ready(f, Duration.Inf)
  }

  {
    val start2 = System.currentTimeMillis()

    val g = for {
      r1 <- Future { Thread.sleep(1000); 10 }
      r2 <- Future { Thread.sleep(1000); 20 }
    } yield {
      val end = System.currentTimeMillis()
      println(s"second - result: ${r1 + r2 }, ${end - start2 }")
    }

    Await.ready(g, Duration.Inf)
  }
}
