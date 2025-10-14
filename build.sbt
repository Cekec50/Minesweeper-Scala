ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.16"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"

lazy val root = (project in file("."))
  .settings(
    name := "Functional-Programming-Project-Minesweeper",
    libraryDependencies += "org.scala-lang.modules" %% "scala-swing" % "3.0.0"
  )
