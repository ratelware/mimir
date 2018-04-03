package com.ratelware.science.slr.ui.bibliography

import com.ratelware.science.slr.ui.html5.FileAPI._
import com.ratelware.science.slr.ui.html5.extensions.FileUID
import com.ratelware.science.slr.ui.navigation.ElementIDs
import com.ratelware.science.slr.ui.quantities.FileSizeFormatter
import org.scalajs.dom.{Element, File, document}
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import scalatags.JsDom._
import scalatags.JsDom.all._
import scalatags.JsDom.tags2._
import org.querki.jquery._

import scala.scalajs.js
import com.ratelware.science.slr.ui.semanticui.Modal._

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.Date


class BibliographyTab(rootElement: Element) {

  val filesBuffer = new ArrayBuffer[(FileUID, File)]()

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
    val target = $(s"#uploadable-bibliography-entries")

    for(i <- 0 until files.length) {
      val file = files.item(i)
      val fileUID = FileUID(file)
      if(!filesBuffer.exists(f => f._1 == fileUID)) {
        filesBuffer.append((fileUID, file))
        target.append(createSummary(file, fileUID.uid))
      }
    }

    $("#bibliography-upload-modal").modal("show")
  }

  private def getDimmer = rootElement.querySelector("#bibliography-upload-dimmer")

  private def createSummary(file: File, uid: String): Element = {
    section(cls := "ui fluid card", id := s"bibliography-update-card-${uid}") (
      div(cls := "content") (
        i(
          cls := "right floated close icon"
        ),
        h3(cls := "header")(file.name),
        div(cls := "meta")(FileSizeFormatter.format(file.size)),
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
            cls := "ui calendar"
          ) (div(
            cls := "fluid ui left icon input"
          ) (
            input(
              tpe := "datetime-local",
              placeholder := "Export date",
              value :=
                new js.Date(file.lastModified).toISOString().substring(0, 16) // UGLEY, but HTML/JS integration apparently sucks
            ),
            i(cls := "calendar alternate icon")
          )
        )))
      )
    ).render
  }
}
