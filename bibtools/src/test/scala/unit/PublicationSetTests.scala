package unit

import com.ratelware.science.bibliography.domain.ops.rules.flattening.{FlatteningError, StrictFlatteningMode}
import com.ratelware.science.bibliography.domain.{DOI, PublicationParam, PublicationSet, PublicationSetDescriptor}
import com.ratelware.science.bibliography.readers.BibTeXReader
import helper.BibTeXFetcher
import org.scalatest.{MustMatchers, OptionValues, TryValues, WordSpec}

class PublicationSetTests extends WordSpec with MustMatchers with TryValues with OptionValues{
  "Publication Set" when {
    val publications1 = BibTeXFetcher.read("two_items.bib")
    val publications2 = BibTeXFetcher.read("two_items_intersecting.bib")
    val publications3 = BibTeXFetcher.read("two_items_similar.bib")
    val publicationsConflicting1 = BibTeXFetcher.read("two_items_conflicting.bib")
    val publicationsConflicting2 = BibTeXFetcher.read("two_items_conflicting2.bib")

    "zipping multiple sets is requested" should {
      "return a result with all versions in all sets" in {
        val zipResult = PublicationSet.zip(Vector(publications1, publications2))
        zipResult.histogram must have size 3

        val duplicate = zipResult.histogram.find(_.headOption.exists(_._2.doi.contains(DOI("10.1002/smr.1581"))))
        duplicate.value must have size 2
        val containingSets = duplicate.value.map(_._1)
        containingSets must contain(PublicationSetDescriptor("0"))
        containingSets must contain(PublicationSetDescriptor("1"))

        zipResult.histogram.filter(_.size == 1) must have size 2
      }
    }

    "flattening ZipResult is requested" should {
      "return first version in optimistic mode" in {
        val flat = PublicationSet.zip(Vector(publications1, publications2))
          .flatten()

        flat.occurrences must have size 3
        val duplicate = flat.occurrences.find(_._2.doi.contains(DOI("10.1002/smr.1581")))
        duplicate.value._1 must have size 2
      }

      "return combination of all versions in strict mode" in {
        val flat = PublicationSet.zip(Vector(publications2, publications3)).flatten(StrictFlatteningMode)
        val duplicate = flat.occurrences.find(_._2.doi.contains(DOI("10.1002/smr.1581")))

        duplicate.value._2.otherParams.contains(PublicationParam.Name("volume")) must be(true)
        duplicate.value._2.otherParams.contains(PublicationParam.Name("url")) must be(true)
      }

      "throw an error if some fields do not match in strict mode" in {
        intercept[FlatteningError] {
          PublicationSet.zip(Vector(publications3, publicationsConflicting1)).flatten(StrictFlatteningMode)
        }
      }
      "throw an error if core fields do not match in strict mode" in {
        intercept[FlatteningError] {
          PublicationSet.zip(Vector(publications3, publicationsConflicting2)).flatten(StrictFlatteningMode)
        }
      }

    }
  }
}
