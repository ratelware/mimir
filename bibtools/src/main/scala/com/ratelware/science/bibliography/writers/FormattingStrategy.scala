package com.ratelware.science.bibliography.writers

import java.io.OutputStream

import com.ratelware.science.bibliography.domain.PublicationSet

import scala.util.Try

trait FormattingStrategy {
  def write(publicationSet: PublicationSet, out: OutputStream): Try[Unit]
  def formatExtension: String
}
