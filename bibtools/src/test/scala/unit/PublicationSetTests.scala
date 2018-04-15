package unit

import com.ratelware.science.bibliography.domain.PublicationSet
import com.ratelware.science.bibliography.readers.BibTeXReader
import org.scalatest.{MustMatchers, TryValues, WordSpec}

class PublicationSetTests extends WordSpec with MustMatchers with TryValues {
  "Publication Set" when {
    "zipping multiple sets is requested" should {
      val publications1 = BibTeXReader
        .read(getClass.getClassLoader.getResource("testdata/bibliography/two_items.bib").openStream())
        .success.value

      val publications2 = BibTeXReader
        .read(getClass.getClassLoader.getResource("testdata/bibliography/two_items_intersecting.bib").openStream())
        .success.value

      "return a result with all versions in all sets" in {
        val zipResult = PublicationSet.zip(Vector(publications1, publications2))

        zipResult.histogram must have size 3
      }

    }
  }
}
