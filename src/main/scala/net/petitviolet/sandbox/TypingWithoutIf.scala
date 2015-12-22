package net.petitviolet.sandbox

import scala.util.Random

object TypingWithoutIf extends App {

  class AdData(id: Long, name: String)

  case class PureData(id: Long, name: String, cost: Int) extends AdData(id, name)

  case class PerformanceData(id: Long, name: String, frequency: Int) extends AdData(id, name)

  trait Service[T <: AdData] {
    def run(adData: T): Unit
  }

  class PureService[T <: PureData] extends Service[T] {
    def run(adData: T): Unit = {
      println("PureService!!!")
      _run(adData)
    }

    private def _run(adData: T): Unit = {
      println(adData)
      println(adData.cost)
    }
  }

  class PerformanceService[T <: PerformanceData] extends Service[T] {
    def run(adData: T): Unit = {
      println("PerformanceService!!!")
      _run(adData)
    }

    private def _run(adData: T): Unit = {
      println(adData)
      println(adData.frequency)
    }
  }

  // 適当なデータ
  case class Creative(id: Long, name: String) {
    val adData: AdData = {
      val value = Random.nextInt(2)
      if (id % 2 == 0) PureData(id, name, value)
      else PerformanceData(id, name, value * 100)
    }
  }

  // 適当なデータ
  val creative = new Creative(Random.nextInt(100), "new-creative")

  val service: Service[_] = creative.adData match {
    case _: PureData => new PureService[PureData]
    case _: PerformanceData => new PerformanceService[PerformanceData]
    case _ => throw new RuntimeException()
  }
//  service.run(creative.adData)

}
