import sbt.Keys._
import pl.project13.scala.sbt.JmhPlugin

val libVersion = "1.0"

scalaVersion := "2.11.7"

val scalazDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.1.1"
)

lazy val root = (project in file("."))
  .settings(name := "scala-sandbox")


lazy val sandbox = (project in file("modules/sandbox"))
  .settings(
    name := "sandbox",
    version := libVersion,
    libraryDependencies ++= scalazDependencies,
    initialCommands += "import scalaz._, Scalaz._"
  )

lazy val performance = (project in file("modules/performance"))
  .enablePlugins(JmhPlugin)
  .settings(
    name := "performance",
    version := libVersion,
    javaOptions in (Jmh, run) ++= Seq("-Xmx1G", "-Dfile.encoding=UTF8")
  )

