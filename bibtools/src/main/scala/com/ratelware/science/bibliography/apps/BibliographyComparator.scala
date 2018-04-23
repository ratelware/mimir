package com.ratelware.science.bibliography.apps

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Files, Path}

import com.github.tototoshi.csv.CSVWriter
import com.ratelware.science.bibliography.domain.ops.rules.zipping.{ZipByCompleteMatch, ZipByDOIOrTitle, ZipByDOIOrTitleAuthorsAndVolume}
import com.ratelware.science.bibliography.domain.{PublicationParam, PublicationSet, PublicationSetDescriptor}
import com.ratelware.science.bibliography.helpers.filesystem.FilenameCreator
import com.ratelware.science.bibliography.readers.BibTeXReader
import com.ratelware.science.bibliography.writers.{BibTeXStrategy, CSVStrategy, FormattingStrategy}

import scala.collection.JavaConverters._

case class BibliographyComparatorConfiguration(inDir: Path, outDir: Path, strategies: Vector[FormattingStrategy])

class BibliographyComparatorCmdParser extends scopt.OptionParser[BibliographyComparatorConfiguration]("bibliography-comparator") {
  opt[File]("inDir").required().action((f, conf) => conf.copy(inDir = f.toPath))
  opt[File]("outDir").required().action((f, conf) => conf.copy(outDir = f.toPath))
  opt[Unit]("csvFormat").optional().action((f, conf) => conf.copy(strategies = conf.strategies :+ CSVStrategy))
  opt[Unit]("bibtexFormat").optional().action((f, conf) => conf.copy(strategies = conf.strategies :+ BibTeXStrategy))
}

object BibliographyComparator extends App {
  val conf = new BibliographyComparatorCmdParser().parse(args, BibliographyComparatorConfiguration(null, null, Vector.empty)).get
  println(s"Using config: $conf")
  val publicationSets = conf.inDir
    .toFile.listFiles()
    .toVector
    .map(f => {
      println(s"Reading ${f}")
      PublicationSetDescriptor(f.getName) -> BibTeXReader.read(new FileInputStream(f)).get
    }).toMap

  val collocations = PublicationSet.zip(publicationSets, ZipByDOIOrTitleAuthorsAndVolume).flatten().bySet()
  val iterableCollocations = collocations.splitted.toVector.sortBy(s => s._1.toVector.map(_.value).sorted.mkString("_"))

  iterableCollocations.foreach(coll => {
    println(s"sets: ${coll._1.toVector.map(_.value).sorted.mkString(",")}, size: ${coll._2.publications.size}")
  })

  conf.strategies.foreach(strategy => {
    val outDir = conf.outDir.resolve(strategy.formatExtension).toFile
    println(s"Using output directory: $outDir for $strategy")
    outDir.mkdirs()
    iterableCollocations.foreach(coll => {
      val outFile = outDir.toPath.resolve(FilenameCreator.createName(coll._1, coll._2, strategy))
      val outStream = new FileOutputStream(outFile.toFile)
      strategy.write(coll._2, outStream)
      outStream.close()
    })
  })

  val csvSummary = conf.outDir.resolve("summary.csv")
  val csvOut = new FileOutputStream(csvSummary.toFile)
  val writer = CSVWriter.open(csvOut)
  writer.writeRow(Vector("Journal", "DOI", "List of authors", "Title", "Abstract"))
  iterableCollocations.foreach(item =>{
    writer.writeRow(Vector(item._1.map(_.value).toVector.mkString(",").sorted, "", "", ""))
    item._2.publications.foreach(p => writer.writeRow(
      Vector(
        p.params.get(PublicationParam.Name("journal")).map(_.value).getOrElse(""),
        p.doi.map(_.value).getOrElse(""),
        p.authors.map(_.name).mkString(";"),
        p.title.value,
        p.params.get(PublicationParam.Name("abstract")).map(_.value).getOrElse("")
      )
    ))
  })
}
