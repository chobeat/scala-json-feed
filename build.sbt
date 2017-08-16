import Dependencies._

lazy val root = (project in file(".")).settings(
  inThisBuild(
    List(
      organization := "org.jsonfeed",
      scalaVersion := "2.12.3",
      version := "0.1.0-SNAPSHOT"
    )),
  name := "scala-json-feed",
  libraryDependencies += scalaTest % Test,
  libraryDependencies ++= circe
)

lazy val reader = (project in file("examples/feed_reader")).settings(
  inThisBuild(
    List(
      organization := "org.jsonfeed",
      scalaVersion := "2.12.3",
      version := "0.1.0"
    )),
  name := "feed-reader"
).dependsOn(root)

homepage := Some(url("https://github.com/chobeat/scala-json-feed"))
scmInfo := Some(ScmInfo(url("https://github.com/chobeat/scala-json-feed"),
  "git@github.com:chobeat/scala-json-feed.git"))
developers += Developer("chobeat",
  "Simone Robutti",
  "simone.robutti@gmail.com",
  url("https://github.com/chobeat"))
licenses += ("GPL", url("https://github.com/chobeat/scala-json-feed/blob/master/LICENSE"))
pomIncludeRepository := (_ => false)