package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSetDescriptor}

case class ZipResult(histogram: Vector[Vector[(PublicationSetDescriptor, Publication)]])
