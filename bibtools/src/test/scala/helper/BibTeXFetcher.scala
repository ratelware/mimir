package helper

import com.ratelware.science.bibliography.domain.PublicationSet
import com.ratelware.science.bibliography.readers.BibTeXReader
import org.scalatest.TryValues

object BibTeXFetcher extends TryValues {
  def read(filename: String): PublicationSet =
    BibTeXReader
      .read(getClass.getClassLoader.getResource(s"testdata/bibliography/$filename").openStream())
      .success.value
}
