package com.ratelware.science.slr.server.management.session

import akka.actor.{ActorPath, LoggingFSM}
import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.{InitializeSession, TerminateSession}

object SessionManager {
  case class Data(activeSessions: Map[SessionId, ActorPath], lastSessionId: Int)
}


class SessionManager extends LoggingFSM[Unit, SessionManager.Data] {

  when(()) {
    case Event(InitializeSession(name, pass), state) =>
      stay using state.copy(
        activeSessions = state.activeSessions + (SessionId((state.lastSessionId + 1).toString) -> self.path),
        lastSessionId = state.lastSessionId + 1
      )

    case Event(TerminateSession(sessionId), state) =>
      stay using state.copy(activeSessions = state.activeSessions - sessionId)
  }

  startWith((), SessionManager.Data(Map.empty, 0))
}
