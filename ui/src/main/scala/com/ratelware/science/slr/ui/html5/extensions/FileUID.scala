package com.ratelware.science.slr.ui.html5.extensions

import org.scalajs.dom.File
import com.ratelware.science.slr.ui.html5.FileAPI._
import org.scalajs.dom.crypto._

import scala.collection.mutable.ArrayBuffer
import scala.scalajs.js.typedarray.ArrayBuffer

case class FileUID(uid: String)
object FileUID {
  private def cleanse(s: String): String = s.replaceAll("[^a-zA-Z0-9_]","")

  def apply(f: File): FileUID = FileUID(cleanse(s"${f.name}-${f.size}-${f.lastModified}"))
}
