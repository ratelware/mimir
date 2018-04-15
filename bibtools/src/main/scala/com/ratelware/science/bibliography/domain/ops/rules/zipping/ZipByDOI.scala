package com.ratelware.science.bibliography.domain.ops.rules

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByDOI extends ZippingRule {
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    sameDois(p1, p2).getOrElse(false)

  def sameDois(p1: Publication, p2: Publication): Option[Boolean] =
    for {
      doi1 <- p1.doi
      doi2 <- p2.doi
    } yield doi1 == doi2
}
