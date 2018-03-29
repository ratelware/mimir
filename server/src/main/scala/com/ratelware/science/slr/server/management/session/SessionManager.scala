package com.ratelware.science.slr.server.management.session

import akka.actor.{ActorPath, ActorRef, LoggingFSM, Props}
import com.ratelware.science.slr.server.management.session.msg.{SessionInitializationResponse, SessionTerminationResponse}
import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.{InitializeSession, TerminateSession}

object SessionManager {
  case class Data(activeSessions: Map[SessionId, ActorRef], nextSessionId: Int)
}

class SessionManager extends LoggingFSM[Unit, SessionManager.Data] {
  when(()) {
    case Event(InitializeSession(name, pass), state) =>
      val newSession = SessionId(state.nextSessionId.toString)
      val sessionHandler = context.actorOf(Props(new SessionHandler(name, newSession)))
      sender ! SessionInitializationResponse(Some(newSession))
      stay using state.copy(
        activeSessions = state.activeSessions + (newSession -> sessionHandler),
        nextSessionId = state.nextSessionId + 1
      )

    case Event(TerminateSession(sessionId), state) =>
      state.activeSessions.get(sessionId).foreach(session => context.stop(session))
      SessionTerminationResponse(state.activeSessions.contains(sessionId))
      stay using state.copy(activeSessions = state.activeSessions - sessionId)
  }

  startWith((), SessionManager.Data(Map.empty, 0))
}
