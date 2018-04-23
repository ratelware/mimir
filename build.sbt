
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
  .settings(libraryDependencies += swagger)
  .dependsOn(shared)

lazy val ui = project
  .settings(commonSettings)
  .settings(name := "scolar-ui")
  .settings(resolvers += artimaResolver)
  .enablePlugins(ScalaJSPlugin, BuildInfoPlugin, GitVersioning)
  .settings(mainClass := Some("com.ratelware.science.slr.ui.WebApp$"))
  .settings(scalaJSUseMainModuleInitializer := true)
  .settings(libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.9.2")
  .settings(libraryDependencies += "com.lihaoyi" %%% "scalatags" % "0.6.7")
  .settings(libraryDependencies += "org.querki" %%% "jquery-facade" % "1.2")
  .settings(libraryDependencies ++= scalaTest)
  .dependsOn(shared)

lazy val bibtools = project
  .settings(commonSettings)
  .settings(name := "bibtools")
  .settings(libraryDependencies += commandLineParser)
  //.settings(libraryDependencies += jbibTeX)
  .settings(libraryDependencies += csvRW)
  .settings(libraryDependencies ++= scalaTest)