package com.ratelware.science.bibliography.domain.ops.rules.flattening

import com.ratelware.science.bibliography.domain.DomainError

case class FlatteningError(msg: String) extends DomainError
