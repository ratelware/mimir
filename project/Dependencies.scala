import sbt._

object Dependencies {
  val akkaHTTP = Seq(
    "com.typesafe.akka" %% "akka-http" % "10.1.0",
    "com.typesafe.akka" %% "akka-http-testkit" % "10.1.0" % Test
  )

  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % "2.5.11"

  val akkaActors = Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.5.11",
    "com.typesafe.akka" %% "akka-stream" % "2.5.11",
    "com.typesafe.akka" %% "akka-slf4j" % "2.5.11",
    "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test
  )

  val jsonSupport = Seq(
    "io.circe" %% "circe-core" % "0.9.1",
    "io.circe" %% "circe-generic" % "0.9.1",
    "io.circe" %% "circe-parser" % "0.9.1",
    "de.heikoseeberger" %% "akka-http-circe" % "1.20.0"
  )

  val swagger = "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.14.0"
  val scalaTest = Seq(
    "org.scalactic" %% "scalactic" % "3.0.5",
    "org.scalatest" %% "scalatest" % "3.0.5" % Test
  )

  val configFilesSupport = Seq(
    "com.github.pureconfig" %% "pureconfig" % "0.9.1"
  )

  val commandLineParser = "com.github.scopt" %% "scopt" % "3.7.0"
  val jbibTeX = "org.jbibtex" % "jbibtex" % "1.0.17"
}
