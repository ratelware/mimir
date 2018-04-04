package com.ratelware.science.slr.ui.semanticui

import org.querki.jquery.JQuery

import scala.language.implicitConversions
import scala.scalajs.js

@js.native
trait Modal extends JQuery {
  def modal(s: String): this.type = js.native
  def modal(b: js.Dictionary[Boolean]): this.type = js.native
}

object Modal {
  implicit def jqToModal(e: JQuery): Modal = e.asInstanceOf[Modal]
}