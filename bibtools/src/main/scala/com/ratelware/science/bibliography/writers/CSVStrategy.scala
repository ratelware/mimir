package com.ratelware.science.bibliography.writers
import java.io.OutputStream

import com.github.tototoshi.csv.CSVWriter
import com.ratelware.science.bibliography.domain.{PublicationParam, PublicationSet}

import scala.util.Try


object CSVStrategy extends FormattingStrategy {
  override def write(publicationSet: PublicationSet, out: OutputStream): Try[Unit] = Try {
    val writer = CSVWriter.open(out)
    val params = publicationSet.publications.map(_.params)
    val paramNames = params.foldLeft(Set.empty[PublicationParam.Name])((acc, v) => acc ++ v.keySet).toVector
    writer.writeRow(paramNames.map(_.name))

    val publications = params.map(p => paramNames.map(n => p.get(n).map(_.value).getOrElse("")))
    writer.writeAll(publications)
  }

  override def formatExtension: String = "csv"
}
