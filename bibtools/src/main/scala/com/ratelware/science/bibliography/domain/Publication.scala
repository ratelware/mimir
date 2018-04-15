package com.ratelware.science.bibliography.domain

import scala.collection.immutable.HashMap

case class Publication(
                        title: Title,
                        authors: Vector[Author],
                        doi: Option[DOI],
                        otherParams: HashMap[PublicationParam.Name, PublicationParam.Value]
                      )