import sbt.Keys._
import pl.project13.scala.sbt.JmhPlugin

val libVersion = "1.0"

val scala = "2.11.7"


val scalazDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.1"
)

def commonSettings(name: String) = Seq(
  scalaVersion := scala,
  version := "1.0",
  libraryDependencies ++= scalazDependencies
)


lazy val root = (project in file("."))
  .settings(commonSettings("scala-sandbox"))


lazy val sandbox = (project in file("modules/sandbox"))
  .settings(commonSettings("sandbox"))
  .settings(
    initialCommands += "import scalaz._, Scalaz._"
  )

lazy val performance = (project in file("modules/performance"))
  .enablePlugins(JmhPlugin)
  .settings(commonSettings("performance"))
  .settings(
    javaOptions in (Jmh, run) ++= Seq("-Xmx2G", "-Dfile.encoding=UTF8")
  )

