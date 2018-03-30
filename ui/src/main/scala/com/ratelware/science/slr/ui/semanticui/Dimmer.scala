package com.ratelware.science.slr.ui.semanticui

import org.querki.jquery.JQuery
import org.scalajs.dom.Element

import scala.language.implicitConversions

import scala.scalajs.js

@js.native
trait Dimmer extends JQuery {
  def dimmer(s: String): this.type = js.native
}

object Dimmer {
  implicit def jqToDimmer(e: JQuery): Dimmer = e.asInstanceOf[Dimmer]
}