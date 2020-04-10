import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "practical-fp",
    libraryDependencies ++= Seq(
      scalaTest % Test,
      compilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
      compilerPlugin("org.augustjune" %% "context-applied" % "0.1.3"),
      "org.typelevel" %% "cats-core" % "2.1.0",
      "org.typelevel" %% "cats-effect" % "2.1.1",
    )
  )

// See https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html for instructions on how to publish to Sonatype.
