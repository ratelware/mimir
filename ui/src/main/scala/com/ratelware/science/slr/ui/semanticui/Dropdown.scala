package com.ratelware.science.slr.ui.semanticui

import org.querki.jquery.JQuery

import scala.language.implicitConversions

import scala.scalajs.js

@js.native
trait Dropdown extends JQuery {
  def dropdown(s: js.Dictionary[Boolean]): this.type = js.native
}

object Dropdown {
  implicit def jqToDropdown(e: JQuery): Dropdown = e.asInstanceOf[Dropdown]
}