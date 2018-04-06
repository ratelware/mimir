package com.ratelware.science.slr.server

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.server.Directives._
import akka.pattern._
import com.ratelware.science.slr.server.api.{SessionAPI, StaticAPI, StudyAPI}
import com.ratelware.science.slr.server.management.session.SessionManager
import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.InitializeSession

import scala.io.StdIn
import scala.util.{Failure, Success}

sealed trait ConfigOption[T]
case object StaticFileDir extends ConfigOption[String]
case object HttpHost extends ConfigOption[String]
case object HttpPort extends ConfigOption[Int]

case class ServerConfig() {
  def get[T](c: ConfigOption[T]): T = {
    (c match {
      case StaticFileDir => Option(System.getProperty("slr.http.staticFileDir")).getOrElse("I:/Ratelware/scolar/static")
      case HttpHost => Option(System.getProperty("slr.http.host")).getOrElse("localhost")
      case HttpPort => Option(System.getProperty("slr.http.port")).getOrElse(9901)
    }).asInstanceOf[T]
  }

}

object APIServer extends App {
  val config = ServerConfig()
  implicit val system = ActorSystem("scolar-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = akka.util.Timeout(5, scala.concurrent.duration.SECONDS)

  val sessionManager = system.actorOf(Props(new SessionManager()))

  val routes =
    StaticAPI(config.get(StaticFileDir)).routes ~
    SessionAPI.routes(sessionManager) ~
    StudyAPI.routes(sessionManager)


  val bindingFuture= Http().bindAndHandle(routes, config.get(HttpHost), config.get(HttpPort))
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
