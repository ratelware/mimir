package com.ratelware.science.bibliography.domain.ops.rules.flattening

import com.ratelware.science.bibliography.domain.Publication
import com.ratelware.science.bibliography.domain.ops.FlatteningMode

object OptimisticFlatteningMode extends FlatteningMode{
  override def flatten(pubs: Vector[Publication]): Publication = {
    if(pubs.size > 2) {
      println(s"Flattening: ${pubs.head.title.value} from ${pubs.size} items")
    }

    require(pubs.nonEmpty)
    pubs.head
  }
}
