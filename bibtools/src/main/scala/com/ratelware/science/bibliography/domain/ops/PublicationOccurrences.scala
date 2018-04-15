package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSetDescriptor}

case class PublicationOccurrences(occurrences: Vector[(Set[PublicationSetDescriptor], Publication)]) {
  def bySet(): PublicationsPerPublicationSet = PublicationsPerPublicationSet (
    occurrences.foldLeft(Map.empty[Set[PublicationSetDescriptor], Set[Publication]])((acc, pub) => {
      acc.updated(pub._1, acc.getOrElse(pub._1, Set.empty) + pub._2)
    })
  )
}