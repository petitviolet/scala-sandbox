import scala.language.{postfixOps, reflectiveCalls}

object DuckTyping extends App {
  sealed abstract class AwesomeData(id: Int)

  case class Nice(id: Int) extends AwesomeData(id) {
    def size: Int = id * 100
  }

  case class Great(id: Int) extends AwesomeData(id) {
    def size: Int = id * 200
  }

  def callSizeReflection[A](a: A) = {
    val method = a.getClass.getMethod("size")
    method.setAccessible(true)
    val size = method.invoke(a)

    size
  }

  def callSizePattern[A](a: A) = {
    val size = a match {
      case nice@Nice(_) => nice.size
      case great@Great(_) => great.size
      case list@(_ : Traversable[_]) => list.size
    }
    size
  }

  type HasSize = { def size: Int }
  def callSizeDuck[A](hasSize: A with HasSize) = {
    hasSize.size
  }

  val nice = Nice(1)
  val great = Great(2)
  val seq = Seq(1, 2, 3)

  println(callSizeReflection(nice))
  println(callSizePattern(nice))
  println(callSizeDuck(nice))

  println(callSizeReflection(great))
  println(callSizePattern(great))
  println(callSizeDuck(great))

  println(callSizeReflection(seq))
  println(callSizePattern(seq))
  println(callSizeDuck(seq))


  def now = System.currentTimeMillis
  def performance(num: Int, func: => Unit) = {
    val start = now
    (0 until num) foreach { _ => func }
    val end = now
    println(s"Time ${end - start}")
  }

  val count = 1000000000
  performance(count, callSizeReflection(nice))
  performance(count, callSizePattern(nice))
  performance(count, callSizeDuck(nice))
}

