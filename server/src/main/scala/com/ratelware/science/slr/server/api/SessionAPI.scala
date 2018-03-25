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
import com.ratelware.science.slr.shared.messages.session.{InitializeSession, TerminateSession}
import akka.pattern._
import com.ratelware.science.slr.server.management.session.msg.{SessionInitializationResponse, SessionTerminationResponse}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.util.{Failure, Success}

object SessionAPI extends FailFastCirceSupport {
  import io.circe.generic.auto._

  def routes(sessionManager: ActorRef)(implicit materializer: ActorMaterializer, requestTimeout: Timeout) = {
    path("login") {
      post {
        entity(as[InitializeSession]) { message =>
          onComplete((sessionManager ? message).mapTo[SessionInitializationResponse]) {
            case Success(SessionInitializationResponse(Some(sessionId))) =>
              complete(HttpResponse(StatusCodes.Created).withHeaders(`Set-Cookie`(HttpCookie("sessionId", sessionId.id))))
            case Success(SessionInitializationResponse(None)) =>
              complete(StatusCodes.Unauthorized)
            case Failure(exception) =>
              complete(StatusCodes.InternalServerError)
          }
        }
      }
    } ~
      path("logout") {
        post {
          entity(as[TerminateSession]) { message =>
            onComplete((sessionManager ? message).mapTo[SessionTerminationResponse]) {
              case Success(SessionTerminationResponse(true)) =>
                complete(StatusCodes.OK)
              case Success(SessionTerminationResponse(false)) =>
                complete(StatusCodes.Forbidden)
              case Failure(exception) =>
                complete(StatusCodes.InternalServerError)
            }
          }

        }
      }

  }
}
