package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport

class ShachiServlet extends ShachiWebAppStack {

  get("/") {
    contentType = "text/html"
    ssp("/index")
  }
  
}
