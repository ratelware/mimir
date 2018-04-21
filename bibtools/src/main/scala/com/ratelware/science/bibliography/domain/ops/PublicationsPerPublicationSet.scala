package com.ratelware.science.bibliography.domain.ops

import com.ratelware.science.bibliography.domain.{Publication, PublicationSet, PublicationSetDescriptor}

case class PublicationsPerPublicationSet(splitted: Map[Set[PublicationSetDescriptor], PublicationSet])