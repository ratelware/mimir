package com.ratelware.science.slr.server

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.http.scaladsl.server.Directives._
import akka.pattern._
import com.ratelware.science.slr.server.api.SessionAPI
import com.ratelware.science.slr.server.management.session.SessionManager
import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.InitializeSession

import scala.io.StdIn
import scala.util.{Failure, Success}


object APIServer extends App {
  implicit val system = ActorSystem("scolar-actors")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher
  implicit val timeout = akka.util.Timeout(5, scala.concurrent.duration.SECONDS)

  val sessionManager = system.actorOf(Props(new SessionManager()))

  val route = SessionAPI.routes(sessionManager)

  val bindingFuture= Http().bindAndHandle(route, "localhost", 9901)
  StdIn.readLine()
  bindingFuture.flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
