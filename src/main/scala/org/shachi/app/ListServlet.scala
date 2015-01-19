package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.shachi.db.DatabaseSessionSupport

class ListServlet extends ShachiWebAppStack with DatabaseSessionSupport {
  get("/") {
    contentType = "text/html"
    ssp("/list/index")
  }
}
