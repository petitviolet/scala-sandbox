package net.petitviolet.sandbox

import java.util.concurrent.ForkJoinPool

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util._

object FutureTest extends App {

  private implicit val ec = ExecutionContext.fromExecutor(new ForkJoinPool(16))

  {
    println(s"---------------------")
    val start1 = System.currentTimeMillis()
    val future1 = Future { Thread.sleep(1000); 10 }
    val future2 = Future { Thread.sleep(1000); 20 }

    val f = for {
      r1 <- future1
      r2 <- future2
    } yield {
      val end = System.currentTimeMillis()
      println(s"first - result: ${r1 + r2}, ${end - start1}")
      // first - result: 30, 1124
    }

    Await.ready(f, Duration.Inf)
  }

  {
    println(s"---------------------")
    val start2 = System.currentTimeMillis()

    val g = for {
      r1 <- Future { Thread.sleep(1000); 10 }
      r2 <- Future { Thread.sleep(1000); 20 }
    } yield {
      val end = System.currentTimeMillis()
      println(s"second - result: ${r1 + r2}, ${end - start2}")
      // second - result: 30, 2007
    }

    Await.ready(g, Duration.Inf)
  }

  {
    println(s"---------------------")
    import Thread.sleep
    val future1 = Future { Thread.sleep(1000); 1 }
    val future2 = Future { Thread.sleep(800); 2 }
    import scala.concurrent.duration._

    val fT1 = Try { Await.result(future1, 800.milliseconds) }
    val fT2 = Try { Await.result(future2, 0.milliseconds) }

    println(s"1: $fT1, 2: $fT2")
    // 1: Failure(java.util.concurrent.TimeoutException: Futures timed out after [800 milliseconds]), 2: Success(2)
  }

  {
    println(s"---------------------")
    val start = System.currentTimeMillis()

    import Thread.sleep
    val g = for {
      (r1, r2) <- Future { Thread.sleep(1000); 10 } zip Future { Thread.sleep(1000); 20 }
    } yield {
      val end = System.currentTimeMillis()
      println(s"second - result: ${r1 + r2}, ${end - start}")
      // second - result: 30, 1035
    }

    Await.ready(g, Duration.Inf)
  }

  private object FutureTrapSample {
    {
      // not in parallel
      import Thread.sleep
      val result: Future[Int] = for {
        a <- Future { sleep(100); 100 }
        b <- Future { sleep(50); 50 }
      } yield a + b
      result onComplete println
    }

    {
      // in parallel
      import Thread.sleep
      // call Future.apply before `for`
      val aF = Future { sleep(100); 100 }
      val bF = Future { sleep(50); 50 }
      val result: Future[Int] = for {
        a <- aF
        b <- bF
      } yield a + b
      result onComplete println
    }

    {
      // in parallel
      import Thread.sleep
      // use `Future#zip`
      val result: Future[Int] = for {
        (a, b) <- Future { sleep(100); 100 } zip Future { sleep(50); 50 }
      } yield a + b
      result onComplete println
    }
  }

}
