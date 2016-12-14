import javax.script._

object NashornExample extends App {
  val ENGINE_NAME = "nashorn"
  val engine = new ScriptEngineManager()
    .getEngineByName(ENGINE_NAME)
    .asInstanceOf[ScriptEngine with Invocable with Compilable]

  case class User(id: Long, name: String)
  val fName = "f"
  val f =
    s"""
       |function $fName(user) {
       |  return "id: " + user.id() + ", name: " + user.name();
       |};
       |""".stripMargin
  engine.compile(f).eval()

  val result1 = engine.invokeFunction(fName, User(1, "alice"))
  println(s"result1 => $result1")

  val gName = "g"
  val g =
    s"""
       |function $gName(id, name) {
       |  var User = Java.type("NashornExample.User");
       |  return new User(id, name);
       |};
       |""".stripMargin

  engine.compile(g).eval()

  val result2 = engine.invokeFunction(gName, Seq(2, "bob") map {_.asInstanceOf[AnyRef]} :_*)
  println(s"result2 => $result2")
  println(s"result2 => ${result2.asInstanceOf[User].name}")
}

