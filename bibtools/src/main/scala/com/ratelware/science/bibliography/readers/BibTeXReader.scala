package com.ratelware.science.bibliography.readers

import java.io.{InputStream, InputStreamReader}

import scala.collection.JavaConverters._
import com.ratelware.science.bibliography.domain._
import org.jbibtex.{BibTeXEntry, BibTeXParser, Key, Value}

import scala.collection.immutable.HashMap
import scala.language.postfixOps
import scala.util.Try

object BibTeXReader {
  def read(inStream: InputStream): Try[PublicationSet] = Try {
    val db = new BibTeXParser().parse(new InputStreamReader(inStream))
    val entries = db.getEntries.asScala.map(p => entryToPublication(p._1, p._2)).toVector
    PublicationSet(entries)
  }

  private def entryToPublication(entryKey: Key, entry: BibTeXEntry): Publication = {
    val title = Title(entry.getField(BibTeXEntry.KEY_TITLE).toUserString)
    val authors = Try {
      entry.getField(BibTeXEntry.KEY_AUTHOR).toUserString.split(" and ").map(Author).toVector
    } getOrElse  Vector.empty

    val doi = Try { DOI(entry.getField(BibTeXEntry.KEY_DOI).toUserString) }.toOption
    Publication(title, authors, doi, bibFieldsToPublicationFields(entryKey, entry))
  }

  private def bibFieldsToPublicationFields(entryKey: Key, entry: BibTeXEntry): HashMap[PublicationParam.Name, PublicationParam.Value] =
    HashMap(
      (entry.getFields.asScala
        .map(e => PublicationParam.Name(e._1.getValue) -> PublicationParam.Value(e._2.toUserString)) +
        (PublicationParam.Name("entryKey") -> PublicationParam.Value(entryKey.getValue)) +
        (PublicationParam.Name("entryType") -> PublicationParam.Value(entry.getType.getValue))
        ).toVector :_*
    )
}
