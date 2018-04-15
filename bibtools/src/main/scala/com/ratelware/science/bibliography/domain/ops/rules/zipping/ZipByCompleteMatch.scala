package com.ratelware.science.bibliography.domain.ops.rules.zipping

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByCompleteMatch extends ZippingRule {
  override def areMatching(p1: Publication, p2: Publication): Boolean = p1 == p2
}
