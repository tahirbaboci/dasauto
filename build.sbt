name := """DasAuto"""
organization := "com.baboci.DasAuto"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies ++= Seq(
  guice,
  filters,
  specs2 % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test,
  "com.typesafe.play" %% "play-slick" % "5.0.0-M4",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0-M4",
  "mysql" % "mysql-connector-java" % "8.0.17"
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.baboci.DasAuto.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.baboci.DasAuto.binders._"
