package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSetDescriptor}

case class PublicationOccurrences(occurrences: Vector[(Set[PublicationSetDescriptor], Publication)])