package com.ratelware.science.bibliography.domain

import com.ratelware.science.bibliography.domain.ops.rules.ZipByDOIOrTitle
import com.ratelware.science.bibliography.domain.ops.{ZipResult, ZippingRule}

case class PublicationSet(publications: Vector[Publication])

object PublicationSet {
  def zip(sets: Map[PublicationSetDescriptor, PublicationSet], rule: ZippingRule): ZipResult =
    ZipResult(
      sets.foldLeft(Vector.empty[Vector[(PublicationSetDescriptor, Publication)]])((acc, pubs) => {
        pubs._2.publications.foldLeft(acc)((lacc, pub) => {
          val matching = lacc.zipWithIndex.find(_._1.forall(p => rule.areMatching(p._2, pub)))
          val toInsert = matching.map(_._1).getOrElse(Vector.empty) :+ (pubs._1, pub)
          matching.map(_._2).map(index => lacc.updated(index, toInsert)).getOrElse(lacc :+ toInsert)
        })
      })
    )

  def zip(sets: Vector[PublicationSet], rule: ZippingRule = ZipByDOIOrTitle): ZipResult =
    zip(sets.zipWithIndex.map(p => PublicationSetDescriptor(p._2.toString) -> p._1).toMap, rule)
}