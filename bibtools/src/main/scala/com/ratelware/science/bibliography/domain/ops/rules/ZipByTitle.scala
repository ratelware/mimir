package com.ratelware.science.bibliography.domain.ops.rules

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByTitle extends ZippingRule {
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    p1.title == p2.title
}
