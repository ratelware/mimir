package com.ratelware.science.bibliography.domain

import com.ratelware.science.bibliography.domain.ops.rules.ZipByDOIOrTitle
import com.ratelware.science.bibliography.domain.ops.{ZipResult, ZippingRule}

case class PublicationSet(publications: Vector[Publication])

object PublicationSet {
  def zip(sets: Vector[PublicationSet], rule: ZippingRule = ZipByDOIOrTitle): ZipResult = {
    ZipResult(
      sets.map(_.publications).zipWithIndex.foldLeft(Vector.empty[Vector[(Int, Publication)]])((acc, pubs) => {
      pubs._1.foldLeft(acc)((lacc, pub) => {
        val matching = lacc.zipWithIndex.find(_._1.forall(p => rule.areMatching(p._2, pub)))
        val toInsert = matching.map(_._1).getOrElse(Vector.empty) :+ (pubs._2, pub)

        matching.map(_._2).map(index => lacc.updated(index, toInsert)).getOrElse(lacc :+ toInsert)
      })
    }))

  }
}