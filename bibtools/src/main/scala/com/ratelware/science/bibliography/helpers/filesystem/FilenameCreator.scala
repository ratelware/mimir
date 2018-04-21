package com.ratelware.science.bibliography.helpers.filesystem

import com.ratelware.science.bibliography.domain.PublicationSetDescriptor
import com.ratelware.science.bibliography.writers.FormattingStrategy

object FilenameCreator {
  def createName(descriptors: Set[PublicationSetDescriptor], formattingStrategy: FormattingStrategy): String =
    descriptors.map(_.value).toVector.sorted.mkString("_and_") + s".${formattingStrategy.formatExtension}"
}
