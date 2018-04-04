package com.ratelware.science.slr.ui.bibliography

import com.ratelware.science.slr.ui.html5.FileAPI._
import com.ratelware.science.slr.ui.html5.extensions.FileUID
import com.ratelware.science.slr.ui.navigation.ElementIDs
import com.ratelware.science.slr.ui.quantities.FileSizeFormatter
import org.scalajs.dom.{Element, File}
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import scalatags.JsDom.all._
import scalatags.JsDom.tags2.section
import org.querki.jquery._

import scala.scalajs.js
import com.ratelware.science.slr.ui.semanticui.Modal._
import com.ratelware.science.slr.ui.semanticui.Dropdown._


class BibliographyTab(rootElement: Element) {
  println("Creating TAB")
  val filesBuffer = new js.Array[(FileUID, File)]()

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
      if(!this.filesBuffer.exists(f => f._1 == fileUID)) {
        filesBuffer.push((fileUID, file))
        target.append(createSummary(file, fileUID))
      }
    }

    $(".ui.dropdown.bibliography-databases").dropdown(js.Dictionary("allowAdditions" -> true))
    $("#bibliography-upload-modal").modal("show")
    getInputElement.value = ""
  }

  def removeBibliographyFile(uid: FileUID) = {
    filesBuffer.splice(filesBuffer.indexWhere(_._1 == uid), 1)
    $(s"#bibliography-update-card-${uid.uid}").hide().remove()
    if(filesBuffer.isEmpty) {
      $("#bibliography-upload-modal").modal("hide")
    }
  }

  private def getDimmer = rootElement.querySelector("#bibliography-upload-dimmer")

  private def createSummary(file: File, uid: FileUID): Element = {
    section(cls := "ui fluid bibliography-update card", id := s"bibliography-update-card-${uid.uid}") (
      div(cls := "content") (
        i(
          cls := "right floated close icon",
          onclick := { () => removeBibliographyFile(uid) }
        ),
        h3(cls := "header")(file.name),
        div(cls := "meta")(FileSizeFormatter.format(file.size)),
        div(cls := "ui description grid form") (
          div( cls := "six wide column") (
          div(
            cls := "required field"
          ) (
            label(i(cls := "database icon"), span("Database")),
            div(
              cls := "ui search selection dropdown bibliography-databases"
            )(
              input(tpe := "hidden", name := "database"),
              i(cls := "dropdown icon"),
              div(cls := "default text")("Database"),
              div(cls := "menu") (
                div(cls := "item", attr("data-value") := "Scopus")("Scopus"),
                div(cls := "item", attr("data-value") := "DBLP")("DBLP")
              )
          )),
          div(
            cls := "ui calendar required field"
          ) (
            label(i(cls := "calendar alternate icon"), span("Export timestamp")),
            input(
              tpe := "datetime-local",
              placeholder := "Export timestamp",
              value :=
                new js.Date(file.lastModified).toISOString().substring(0, 16) // UGLEY, but HTML/JS integration apparently sucks
            )
          )
        ),
        div(
          cls := "ten wide column"
        )(
        div(
          cls := "field"
        ) (
          label(i(cls := "file code icon"), span("Search string")),
          textarea(
            rows := 5,
            placeholder := "(Optional) Search string"
          )
        ))
        )
      )
    ).render
  }
}
