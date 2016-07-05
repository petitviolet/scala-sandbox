package net.petitviolet.sandbox

object TypeClassTest extends App {
  trait Target[A] {
    def createKey(key: A): A
  }

  case class IntTarget() extends Target[Int] {
    override def createKey(key: Int): Int = {
      println(s"key: $key")
      key * 100
    }
  }
  case class DoubleTarget() extends Target[Double] {
    override def createKey(key: Double): Double = {
      println(s"key: $key")
      key * 1000
    }
  }

  trait TypeHelper[A] {
    def run: String => A
  }

  case class IntTypeHelper() extends TypeHelper[Int] {
    override def run: String => Int = _.toInt
  }

  case class DoubleTypeHelper() extends TypeHelper[Double] {
    override def run: String => Double = _.toDouble
  }

  implicit val toIntConverter = IntTypeHelper()
  implicit val toDoubleConverter = DoubleTypeHelper()

  def createTargetKey[A](target: Target[A], key: String)(implicit helper: TypeHelper[A]) = {
    target.createKey(helper.run(key))
  }

  createTargetKey(IntTarget(), "10")
  createTargetKey(DoubleTarget(), "100.0")
}
