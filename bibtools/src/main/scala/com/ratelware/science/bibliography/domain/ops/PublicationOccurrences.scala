package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSet, PublicationSetDescriptor}

case class PublicationOccurrences(occurrences: Vector[(Set[PublicationSetDescriptor], Publication)]) {
  def bySet(): PublicationsPerPublicationSet = PublicationsPerPublicationSet (
    occurrences.foldLeft(Map.empty[Set[PublicationSetDescriptor], Vector[Publication]])((acc, pub) => {
      acc.updated(pub._1, acc.getOrElse(pub._1, Vector.empty) :+ pub._2)
    }).map(p => p._1 -> PublicationSet(p._2))
  )
}