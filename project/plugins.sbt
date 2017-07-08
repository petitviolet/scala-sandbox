logLevel := Level.Warn

resolvers ++= Seq(
  Resolver.bintrayIvyRepo("scalameta", "maven"),
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)

// JMH
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.20")

addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.6.0")