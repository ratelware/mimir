package com.ratelware.science.slr.ui
import com.ratelware.science.slr.ui.bibliography.BibliographyTab
import com.ratelware.science.slr.ui.navigation.ElementIDs
import org.scalajs.dom._

object WebApp {
  def main(args: Array[String]): Unit = {
    new BibliographyTab(document.getElementById(ElementIDs.BIBLIOGRAPHY_SECTION)).setupUI
  }
}
