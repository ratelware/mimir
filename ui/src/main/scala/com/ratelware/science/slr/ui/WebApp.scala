package com.ratelware.science.slr.ui
import org.scalajs.dom._

import scala.scalajs.js.annotation.JSExportTopLevel

object WebApp {
  @JSExportTopLevel("WebApp.main")
  def main(): Unit = {
    document.getElementById("main-display")
      .appendChild(NagelSchreckenbergController.setupUI())
  }
}
