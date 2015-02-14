package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.squeryl.PrimitiveTypeMode._
import org.shachi.db.DatabaseSessionSupport
import org.shachi.schema.Annotator
import org.shachi.schema.Resource
import org.shachi.model.AnnotatorId

class EditServlet extends ShachiWebAppStack with DatabaseSessionSupport {
  private val defaultLayout = "WEB-INF/templates/layouts/edit.ssp"

  get("/") {
    inTransaction {
      val annotators = Annotator.selectAll
      val countByAnnotatorId = Resource.countByAnnotatorId
      val aid = new AnnotatorId(params.getOrElse("aid", "0").toInt)
      val resources = Resource.selectByAnnotatorId(aid)

      contentType = "text/html"
      ssp("/edit/index",
          "layout" -> defaultLayout,
          "annotators" -> annotators,
          "countByAnnotatorId" -> countByAnnotatorId,
          "resources" -> resources
      )
    }
  }
}
