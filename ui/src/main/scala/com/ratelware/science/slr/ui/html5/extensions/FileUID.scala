package com.ratelware.science.slr.ui.html5.extensions

import org.scalajs.dom.File
import com.ratelware.science.slr.ui.html5.FileAPI._


case class FileUID(uid: String)
object FileUID {
  private def cleanse(s: String): String = s
    .replaceAllLiterally(".", "_")
    .replaceAllLiterally("?", "_")
    .replaceAllLiterally("*", "_")
    .replaceAllLiterally("$", "_")
    .replaceAllLiterally("^", "_")


  def apply(f: File): FileUID = FileUID(cleanse(s"${f.name}-${f.size}-${f.lastModified}"))
}
