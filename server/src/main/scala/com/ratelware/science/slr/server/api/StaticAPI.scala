package com.ratelware.science.slr.server.api

import akka.http.scaladsl.server.Directives._

case class StaticAPI(staticRoot: String) {
  def routes =
    path("static" / Remaining) { filename => getFromFile(staticRoot + "/" + filename)}

}
