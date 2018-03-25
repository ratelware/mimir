package com.ratelware.science.slr.server.management.session.msg

import com.ratelware.science.slr.shared.messages.session.Message

case class SessionTerminationResponse(success: Boolean) extends Message
