package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.Publication

case class ZipResult(histogram: Vector[Vector[(Int, Publication)]])
