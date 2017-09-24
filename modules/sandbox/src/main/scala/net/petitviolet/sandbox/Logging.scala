package net.petitviolet.sandbox

trait Logging {
  def logging[A](msg: A)(implicit line: sourcecode.Line, enclosing: sourcecode.Enclosing): Unit = {
    // add logging tag
    val clazz = enclosing.value.split('.').last
    val tag = s"<$clazz:${line.value }>"
    println(s"$tag $msg")
  }

  implicit class Any4Logging[A](val a: A) {
    def log()(implicit line: sourcecode.Line, enclosing: sourcecode.Enclosing): A =
    { logging(a)(line, enclosing); a }
  }
}
