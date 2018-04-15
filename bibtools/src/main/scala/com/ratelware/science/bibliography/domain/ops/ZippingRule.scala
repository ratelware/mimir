package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.Publication

trait ZippingRule {

  /** Must be reflexive and transitive!
    *
    * @param p1
    * @param p2
    * @return
    */
  def areMatching(p1: Publication, p2: Publication): Boolean
}
