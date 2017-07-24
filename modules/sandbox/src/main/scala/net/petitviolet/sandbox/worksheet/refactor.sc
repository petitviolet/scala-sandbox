import java.util

import scala.collection.JavaConverters._

object Main extends App {

  case class GroupId(value: String)
  class StringValue(v: String)
  case class Request(s1: util.List[StringValue], s2: util.List[StringValue], s3: util.List[StringValue])

  val l1 = GroupId("a") :: GroupId("b") :: GroupId("c") :: Nil
  val l2 = GroupId("x") :: GroupId("y") :: Nil
  val l3 = "cat" :: "dog" :: "pig" :: Nil

  private def toStringValues[A](as: Seq[A])(f: A => String): util.List[StringValue] = {
    val g: A => StringValue = f andThen { new StringValue(_) }
    as.map { g }.asJava
  }

  val r = Request(
    toStringValues(l1)(_.value),
    toStringValues(l2)(_.value),
    toStringValues(l3)(identity)
  )

  def f(xs:Seq[Any]): Seq[String] = {
    xs.map { x: Any =>
      x match {
        case groupId: GroupId => groupId.value
        case str: String => str
      }
    }
  }
  def g(xs:Seq[Any]): Seq[String] = {
    xs.map {
      case groupId: GroupId => groupId.value
      case str: String => str
    }
  }

  println(r)
}

Main.main(Array[String]())

