package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.squeryl.PrimitiveTypeMode._
import org.shachi.db.DatabaseSessionSupport
import org.shachi.schema.{Annotator,Resource,Metadata,MetadataValue}
import org.shachi.model.{AnnotatorId,ResourceId}

class EditServlet extends ShachiWebAppStack with DatabaseSessionSupport {
  private val defaultLayout = "WEB-INF/templates/layouts/edit.ssp"

  get("/") {
    inTransaction {
      val annotators = Annotator.selectAll
      val countByAnnotatorId = Resource.countByAnnotatorId
      val resources = params.get("aid").map{ aid =>
        val annotatorId = AnnotatorId(aid.toInt)
        if (annotatorId.value == 0)
          Resource.selectAll
        else
          Resource.selectByAnnotatorId(annotatorId)
      }.getOrElse(List())

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
      val resourceId = ResourceId(id.toLong)

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

  get("""/edit/(\d+)""".r) {
    inTransaction {
      val id = multiParams("captures").head
      val resourceId = ResourceId(id.toLong)

      Resource.selectDetailById(resourceId).fold(NotFound("Resource not found")) { resource =>
        contentType = "text/html"
        Ok(ssp("/edit/edit",
          "layout" -> defaultLayout,
          "resource" -> resource,
          "metadata" -> Metadata.selectShown,
          "annotators" -> Annotator.selectAll,
          "valuesByType" -> MetadataValue.selectExcludeLangauge.groupBy(_.valueType)
        ))
      }
    }
  }
}
