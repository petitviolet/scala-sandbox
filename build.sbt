import pl.project13.scala.sbt.JmhPlugin
import sbt.Keys._

val libVersion = "1.0"

val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.14",
  "com.chuusai" %% "shapeless" % "2.3.2"
)

val scala = "2.12.2"

def commonSettings(name: String) = Seq(
  scalaVersion := scala,
  version := "1.0",
  libraryDependencies ++= commonDependencies,
  libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"
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
    javaOptions in(Jmh, run) ++= Seq("-Xmx2G", "-Dfile.encoding=UTF8")
  )

