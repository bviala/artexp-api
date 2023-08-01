name := """artexp-api"""
organization := "churros-software"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.11"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
libraryDependencies +=("org.playframework.anorm" %% "anorm" % "2.7.0")

// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.6.0"

libraryDependencies ++= Seq(evolutions, jdbc)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "churros-software.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "churros-software.binders._"
