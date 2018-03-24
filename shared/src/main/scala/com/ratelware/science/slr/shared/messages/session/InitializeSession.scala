package com.ratelware.science.slr.shared.messages.session

import com.ratelware.science.slr.shared.definitions.{Password, Username}

case class InitializeSession(username: Username, password: Password) extends SessionMessages
