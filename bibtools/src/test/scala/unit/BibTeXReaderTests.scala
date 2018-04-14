package unit

import java.io.ByteArrayInputStream
import java.nio.charset.Charset

import com.ratelware.science.bibliography.domain.{DOI, Title}
import com.ratelware.science.bibliography.readers.BibTeXReader
import org.scalatest.{MustMatchers, TryValues, WordSpec}

class BibTeXReaderTests extends WordSpec with MustMatchers with TryValues {
  "BibTeX reader" when {
    "reads a malformed stream" should {
      "return a Failure" in {
        val inStream = new ByteArrayInputStream("Invalid-bbitex-f@aa{e=}".getBytes(Charset.forName("UTF-8")))
        val publications = BibTeXReader.read(inStream)
        publications.isFailure must be(true)
      }
    }

    "reads a valid BibTeX stream" should {
      val inStream = getClass.getClassLoader.getResource("testdata/bibliography/two_items.bib").openStream()
      val publications = BibTeXReader.read(inStream)


      "be able to calculate bibliography size" in {
        publications.success.value.publications must have size 2
      }
      "be able to list all publications" in {
        val pubs = publications.success.value
        pubs.publications.find(_.title == Title("Automatic segmentation of method code into meaningful blocks: Design and evaluation")) mustNot be(empty)
        pubs.publications.find(_.doi.contains(DOI("10.1002/smr.1581"))) mustNot be(empty)



      }
    }
  }
}
