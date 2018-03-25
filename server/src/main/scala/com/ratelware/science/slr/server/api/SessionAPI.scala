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
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport

import scala.util.{Failure, Success}

object SessionAPI extends FailFastCirceSupport {
  import io.circe.generic.auto._

  def routes(sessionManager: ActorRef)(implicit materializer: ActorMaterializer, requestTimeout: Timeout) = {
    path("login") {
      post {
        entity(as[InitializeSession]) { message =>
          onComplete((sessionManager ? message).mapTo[Option[SessionId]]) {
            case Success(Some(sessionId)) =>
              complete(HttpResponse(StatusCodes.Created).withHeaders(`Set-Cookie`(HttpCookie("sessionId", sessionId.id))))
            case Success(None) =>
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
            onComplete((sessionManager ? message).mapTo[Option[Unit]]) {
              case Success(Some(())) =>
                complete(StatusCodes.OK)
              case Success(None) =>
                complete(StatusCodes.Forbidden)
              case Failure(exception) =>
                complete(StatusCodes.InternalServerError)
            }
          }

        }
      }

  }
}
