package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.shachi.db.DatabaseSessionSupport

class EditServlet extends ShachiWebAppStack with DatabaseSessionSupport {
  get("/") {
    contentType = "text/html"
    ssp("/edit/index")
  }
}
