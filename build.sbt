import sbt.Keys._

val libVersion = "1.0"

val scala = "2.12.1"

val scalazDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.8"
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

