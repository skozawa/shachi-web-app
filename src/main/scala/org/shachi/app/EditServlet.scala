package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.squeryl.PrimitiveTypeMode._
import org.shachi.db.DatabaseSessionSupport
import org.shachi.schema.{Annotator,Resource,Metadata}
import org.shachi.model.{AnnotatorId,ResourceId}

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

  get("""/detail/(\d+)""".r) {
    inTransaction {
      val id = multiParams("captures").head
      val resourceId = new ResourceId(id.toLong)

      Resource.selectDetailById(resourceId).fold(NotFound("Resouce not found")){resource =>
        contentType = "text/html"
        Ok(ssp("/edit/detail",
            "layout" -> defaultLayout,
            "resource" -> resource,
            "metadata" -> Metadata.selectShown,
            "annotatorOpt" -> Annotator.selectById(resource.resource.annotatorId)
        ))
      }
    }
  }
}
