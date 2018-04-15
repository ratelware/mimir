package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSetDescriptor}

case class PublicationsPerPublicationSet(splitted: Map[Set[PublicationSetDescriptor], Set[Publication]])