package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.ops.rules.flattening.OptimisticFlatteningMode
import com.ratelware.science.bibliography.domain.{Publication, PublicationSetDescriptor}

case class ZipResult(histogram: Vector[Vector[(PublicationSetDescriptor, Publication)]]) {
  def flatten(flatteningMode: FlatteningMode = OptimisticFlatteningMode): PublicationOccurrences =
    PublicationOccurrences(histogram.map(p => (p.map(_._1).toSet, flatteningMode.flatten(p.map(_._2)))))
}
