package com.ratelware.science.slr.server.management.session

import akka.actor.Actor
import com.ratelware.science.slr.shared.definitions.{SessionId, Username}

class SessionHandler(user: Username, id: SessionId) extends Actor {
  override def receive: Receive = {
    case _ => ()
  }
}

