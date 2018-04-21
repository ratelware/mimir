package com.ratelware.science.bibliography.domain.ops.rules.zipping

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByTitleAndAuthors extends ZippingRule{
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    ZipByTitle.areMatching(p1, p2) && ZipByAuthors.areMatching(p1, p2)
}
