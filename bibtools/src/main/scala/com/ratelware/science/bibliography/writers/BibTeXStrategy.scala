package com.ratelware.science.bibliography.writers

import java.io.{OutputStream, OutputStreamWriter}

import scala.collection.JavaConverters._
import com.ratelware.science.bibliography.domain._
import org.jbibtex._
import org.jbibtex.policies.BibTeXEntryKeyConflictResolutionPolicies

import scala.language.postfixOps
import scala.util.Try
import scala.language.implicitConversions

object BibTeXStrategy extends FormattingStrategy {
  def write(publicationSet: PublicationSet, out: OutputStream): Try[Unit] = Try {
    val db = new BibTeXDatabase(BibTeXEntryKeyConflictResolutionPolicies.REKEY_SUBSEQUENT)
    publicationSet.publications.map(p => publicationToBibTeXEntry(p)).foreach(e => db.addObject(e))

    new BibTeXFormatter().format(db, new OutputStreamWriter(out))
  }

  private def publicationToBibTeXEntry(p: Publication): BibTeXEntry = {
    val entry = new BibTeXEntry(
      new Key(p.params(PublicationParam.Names.ENTRY_TYPE).value),
      new Key(p.params(PublicationParam.Names.ENTRY_KEY).value)
    )

    entry.addAllFields(p.params.map(par => new Key(par._1.name) -> new StringValue(par._2.value, StringValue.Style.BRACED).asInstanceOf[Value]).asJava)
    entry
  }

  override def formatExtension: String = "bib"
}
