package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.shachi.db.DatabaseSessionSupport

class Statistics extends ShachiWebAppStack with DatabaseSessionSupport {
  get("/") {
    contentType = "text/html"
    ssp("/statistics/index")
  }
}
