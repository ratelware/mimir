package com.ratelware.science.bibliography.apps

import java.io.{File, FileInputStream, FileOutputStream}
import java.nio.file.{Files, Path}

import com.ratelware.science.bibliography.domain.ops.rules.zipping.{ZipByDOIOrTitle, ZipByDOIOrTitleAndAuthors}
import com.ratelware.science.bibliography.domain.{PublicationSet, PublicationSetDescriptor}
import com.ratelware.science.bibliography.helpers.filesystem.FilenameCreator
import com.ratelware.science.bibliography.readers.BibTeXReader
import com.ratelware.science.bibliography.writers.{BibTeXStrategy, CSVStrategy}

import scala.collection.JavaConverters._

case class BibliographyComparatorConfiguration(inDir: Path, outDir: Path)

class BibliographyComparatorCmdParser extends scopt.OptionParser[BibliographyComparatorConfiguration]("bibliography-comparator") {
  opt[File]("inDir").required().action((f, conf) => conf.copy(inDir = f.toPath))
  opt[File](name="outDir").required().action((f, conf) => conf.copy(outDir = f.toPath))
}

object BibliographyComparator extends App {
  val conf = new BibliographyComparatorCmdParser().parse(args, BibliographyComparatorConfiguration(null, null)).get



  val publicationSets = conf.inDir
    .toFile.listFiles()
    .toVector
    .map(f => PublicationSetDescriptor(f.getName) -> BibTeXReader.read(new FileInputStream(f)).get).toMap
  val collocations = PublicationSet.zip(publicationSets, ZipByDOIOrTitleAndAuthors).flatten().bySet()
  collocations.splitted.foreach(coll => {
    val bibFile = conf.outDir.resolve(FilenameCreator.createName(coll._1, BibTeXStrategy))
    val csvFile = conf.outDir.resolve(FilenameCreator.createName(coll._1, CSVStrategy))

    val bibOut = new FileOutputStream(bibFile.toFile)
    val csvOut = new FileOutputStream(csvFile.toFile)

    BibTeXStrategy.write(coll._2, bibOut)
    CSVStrategy.write(coll._2, csvOut)

    bibOut.close()
    csvOut.close()
  })

}
