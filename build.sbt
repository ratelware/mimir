import Resolvers._
import Dependencies._

name := "scolar"
lazy val commonSettings = Seq(scalaVersion := "2.12.5")

lazy val shared = project
  .settings(commonSettings)
  .settings(name := "scolar-shared")
  .enablePlugins(BuildInfoPlugin, GitVersioning)

lazy val server = project
  .settings(commonSettings)
  .settings(name := "scolar-server")
  .settings(resolvers += artimaResolver)
  .enablePlugins(BuildInfoPlugin, GitVersioning)
  .settings(libraryDependencies ++= akkaHTTP)
  .settings(libraryDependencies ++= akkaActors)
  .settings(libraryDependencies += akkaPersistence)
  .settings(libraryDependencies ++= scalaTest)
  .settings(libraryDependencies ++= configFilesSupport)
  .settings(libraryDependencies += commandLineParser)
  .settings(libraryDependencies ++= jsonSupport)
  .dependsOn(shared)

lazy val ui = project
  .settings(commonSettings)
  .settings(name := "scolar-ui")
  .settings(resolvers += artimaResolver)
  .enablePlugins(ScalaJSPlugin, BuildInfoPlugin, GitVersioning)
  .settings(scalaJSUseMainModuleInitializer := true)
  .settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2")
  .settings(libraryDependencies ++= scalaTest)
  .dependsOn(shared)
