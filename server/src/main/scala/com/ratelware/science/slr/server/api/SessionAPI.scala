package com.ratelware.science.slr.server.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.model.headers.{HttpCookie, `Set-Cookie`}
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.ratelware.science.slr.server.APIServer.sessionManager
import com.ratelware.science.slr.server.management.session.SessionManager
import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.InitializeSession
import akka.pattern._
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.util.{Failure, Success}

object SessionAPI extends FailFastCirceSupport {
  import io.circe.generic.auto._

  def routes(sessionManager: ActorRef)(implicit materializer: ActorMaterializer, requestTimeout: Timeout) = {
    path("login") {
      post {
        entity(as[InitializeSession]) { message =>
          onComplete((sessionManager ? message).mapTo[Option[SessionId]]) {
            case Success(sessionId) =>
              val response = sessionId.map(id =>
                HttpResponse(StatusCodes.OK).withHeaders(`Set-Cookie`(HttpCookie("sessionId", id.id)))
              ).getOrElse(HttpResponse(StatusCodes.Unauthorized))

              complete(response)
            case Failure(exception) =>
              complete(HttpResponse(StatusCodes.InternalServerError))
          }
        }
      }
    } ~
      path("logout") {
        complete("")
      }

  }
}
