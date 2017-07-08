import sbt.Keys._

val libVersion = "1.0"

val scala = "2.12.1"

val commonDependencies = Seq(
  "org.scalaz" %% "scalaz-core" % "7.2.8",
  "com.chuusai" %% "shapeless" % "2.3.2"
)

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

