package com.ratelware.science.slr.ui.html5

import org.scalajs.dom.raw.File

import scala.scalajs.js

import scala.language.implicitConversions

@js.native
trait FileAPI extends js.Any {
  def lastModified: Double = js.native
}

object FileAPI {
  implicit def fileToOptionalAPI(f: File): FileAPI = f.asInstanceOf[FileAPI]
}