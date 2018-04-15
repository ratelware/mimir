package com.ratelware.science.bibliography.domain.ops.rules

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByDOIOrTitle extends ZippingRule{
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    ZipByDOI.sameDois(p1, p2)
      .getOrElse(ZipByTitle.areMatching(p1, p2))
}
