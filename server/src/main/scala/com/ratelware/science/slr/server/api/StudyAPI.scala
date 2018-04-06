package com.ratelware.science.slr.server.api

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.util.Timeout
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import constants._

object StudyAPI extends FailFastCirceSupport{
  def routes(sessionManager: ActorRef)(implicit materializer: ActorMaterializer, requestTimeout: Timeout) = {
    cookie(SESSION_COOKIE_NAME) { sessionId =>
      path("studies" / IntNumber) { studyId =>
        complete("")
      }
    }
  }
}
