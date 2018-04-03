package com.ratelware.science.slr.ui.html5.extensions

import org.scalajs.dom.File
import com.ratelware.science.slr.ui.html5.FileAPI._


case class FileUID(uid: String)
object FileUID {
  def apply(f: File): FileUID = FileUID(s"${f.name}-${f.size}-${f.lastModified}")
}
