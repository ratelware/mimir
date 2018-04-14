package com.ratelware.science.bibliography.domain

case class Publication(
                        title: Title,
                        authors: Vector[Author],
                        doi: Option[DOI],
                        otherParams: Map[PublicationParam.Name, PublicationParam.Value]
                      )