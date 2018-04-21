package com.ratelware.science.bibliography.domain.ops.rules.zipping

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByDOIOrTitleAndAuthors extends ZippingRule{
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    ZipByDOI.sameDois(p1, p2)
      .getOrElse(ZipByTitleAndAuthors.areMatching(p1, p2))
}
