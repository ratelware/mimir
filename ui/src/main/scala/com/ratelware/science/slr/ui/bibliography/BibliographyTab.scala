package com.ratelware.science.slr.ui.bibliography

import com.ratelware.science.slr.ui.navigation.ElementIDs
import com.ratelware.science.slr.ui.semanticui.Dimmer
import org.scalajs.dom.{Element, File, document}
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import scalatags.JsDom._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2._
import org.querki.jquery._

import scala.scalajs.js
import Dimmer._

class BibliographyTab(rootElement: Element) {
  private def getInputElement: HTMLInputElement = {
    val inputElementCandidate = rootElement.querySelector(s"#${ElementIDs.BIBLIOGRAPHY_FILE_UPLOAD}")
    require(
      inputElementCandidate.isInstanceOf[HTMLInputElement],
      s"${ElementIDs.BIBLIOGRAPHY_FILE_UPLOAD} must be an input, got: ${inputElementCandidate}"
    )
    inputElementCandidate.asInstanceOf[HTMLInputElement]
  }

  def setupUI(): Unit = {
    getInputElement.onchange = displayUploadCandidates
  }

  def displayUploadCandidates(ev: Event): Unit = {
    val files = getInputElement.files
    val target = rootElement.querySelector(s"#uploadable-bibliography-entries")

    for(i <- 0 until files.length) {
      println(files.item(i))
      target.appendChild(createSummary(files.item(i)))
    }

    $("#bibliography-upload-dimmer").dimmer("show")
  }

  private def getDimmer = rootElement.querySelector("#bibliography-upload-dimmer")

  private def createSummary(file: File): Element = {
    section(cls := "ui fluid card") (
      div(cls := "content") (
        i(
          cls := "right floated close icon"
        ),
        h3(cls := "header")(file.name),
        div(cls := "meta")(file.size + " bytes"),
        div(cls := "description")(
          p(
          div(
            cls := "fluid ui left icon input"
          ) (
            input(
              tpe := "text",
              placeholder := "Database"
            ),
            i( cls := "database icon")
          )),
          p(div(
            cls := "fluid ui left icon input"
          ) (
            input(
              tpe := "text",
              placeholder := "Search string"
            ),
            i(cls := "file code icon")
          )),
          p(div(
            cls := "fluid ui left icon input"
          ) (
            input(
              tpe := "date",
              placeholder := "Export date"
            ),
            i(cls := "calendar alternate icon")
          )
        ))
      )
    ).render
  }
}
