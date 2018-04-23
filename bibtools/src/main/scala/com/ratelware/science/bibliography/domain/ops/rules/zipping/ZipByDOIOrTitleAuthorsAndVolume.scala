package com.ratelware.science.bibliography.domain.ops.rules.zipping

import com.ratelware.science.bibliography.domain.{Publication, PublicationParam}
import com.ratelware.science.bibliography.domain.ops.ZippingRule

object ZipByDOIOrTitleAuthorsAndVolume extends ZippingRule{
  override def areMatching(p1: Publication, p2: Publication): Boolean =
    ZipByDOI.sameDois(p1, p2).getOrElse(ZipByTitleAndAuthors.areMatching(p1, p2)) &&
      p1.params.get(PublicationParam.Name("volume")) == p2.params.get(PublicationParam.Name("volume"))
}
