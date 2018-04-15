package com.ratelware.science.bibliography.domain.ops.rules

import com.ratelware.science.bibliography.domain.Publication

trait FlatteningMode {
  def flatten(pubs: Vector[Publication])
}
