name := """todo_pet"""
organization := "raiffeisen"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  guice, 
  jdbc, 
  evolutions, 
  "com.h2database"          %  "h2"                 % "1.4.197",
  "org.playframework.anorm" %% "anorm"              % "2.6.1",
  "org.scalatestplus.play"  %% "scalatestplus-play" % "3.1.2"     % Test
)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "raiffeisen.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "raiffeisen.binders._"
