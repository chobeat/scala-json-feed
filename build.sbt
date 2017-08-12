import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "org.jsonfeed",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "scala-json-feed",
    libraryDependencies += scalaTest % Test,

    libraryDependencies ++= circe



  )
