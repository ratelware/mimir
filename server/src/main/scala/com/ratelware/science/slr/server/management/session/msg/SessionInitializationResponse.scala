package com.ratelware.science.slr.server.management.session.msg

import com.ratelware.science.slr.shared.definitions.SessionId
import com.ratelware.science.slr.shared.messages.session.Message

case class SessionInitializationResponse(sessionId: Option[SessionId]) extends Message