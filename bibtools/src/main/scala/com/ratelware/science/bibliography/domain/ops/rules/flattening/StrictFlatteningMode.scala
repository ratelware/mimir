package com.ratelware.science.bibliography.domain.ops.rules.flattening

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.FlatteningMode

object StrictFlatteningMode extends FlatteningMode{
  override def flatten(pubs: Vector[Publication]): Publication = {
    require(pubs.nonEmpty)
    pubs.reduce((acc, p1) => {
      if(acc.title != p1.title || acc.doi != p1.doi || acc.authors != p1.authors) {
        throw FlatteningError(s"Publication core data does not match: ${acc} vs ${p1}")
      }

      val params = acc.otherParams.merged(p1.otherParams)((a, b) => {
        if(a._2 != b._2) {
          throw FlatteningError(s"Values at ${a._1} and ${b._1} do not match (${a._2} vs ${b._2})")
        }

        a._1 -> a._2
      })

      acc.copy(otherParams = params)
    })
  }
}
