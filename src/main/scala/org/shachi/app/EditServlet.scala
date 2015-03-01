package org.shachi.app

import org.scalatra._
import scalate.ScalateSupport
import org.squeryl.PrimitiveTypeMode._
import org.shachi.db.DatabaseSessionSupport
import org.shachi.schema.{Annotator,Resource,Metadata,MetadataValue,Language}
import org.shachi.model.{AnnotatorId,ResourceId,MetadataInputType,MetadataValueId}
import org.shachi.model.{Metadata => MetadataModel}
import org.shachi.model.ResourceDetails._

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

  post("""/confirm/(\d+)""".r) {
    inTransaction {
      val id = multiParams("captures").head
      val resourceId = ResourceId(id.toLong)

      Resource.selectById(resourceId).fold(NotFound("Resource not found")){ resource =>
        val metadataList = Metadata.selectShown
        val title = params.getOrElse("title", "")
        val annotatorId = AnnotatorId(params.getOrElse("annotator", "1").toLong) // fallback to administrator
        val newResource = resource.copy(title = title, annotatorId = annotatorId)

        contentType = "text/html"
        Ok(ssp("/edit/confirm",
          "layout" -> defaultLayout,
          "resource" -> ResourceDetail(newResource, valuesFromParams(metadataList)),
          "metadata" -> metadataList,
          "annotatorOpt" -> Annotator.selectById(newResource.annotatorId)
        ))
      }
    }
  }

  post("""/update/(\d+)""".r) {
    inTransaction {
      val id = multiParams("captures").head
      val resourceId = ResourceId(id.toLong)

      Resource.selectById(resourceId).fold(NotFound("Resource not found")){ resource =>
        val metadataList = Metadata.selectShown
        val title = params.getOrElse("title", "")
        val annotatorId = AnnotatorId(params.getOrElse("annotator", "1").toLong) // fallback to administrator
        val values = valuesFromParams(metadataList)

        val needsUpdateResource: Boolean =
          resource.title != title || resource.annotatorId != annotatorId
        if ( needsUpdateResource ) {
          Resource.updateTitleAndAnnotator(resource.id, title, annotatorId)
        }

        val hasChangeMetadataValue = Resource.updateResourceMetadata(
          resourceId = resource.id,
          metadataIds = metadataList.map(_.id),
          newValues = values
        )

        if (needsUpdateResource || hasChangeMetadataValue) {
          Resource.updateModified(resource.id)
        }

        redirect(resource.editDetailLink)
      }
    }
  }


  private def toMetadataValueId(s: String): Option[MetadataValueId] = {
    try {
      Some(MetadataValueId(s.toLong))
    } catch {
      case e: Exception => None
    }
  }

  private def valuesFromParams(metadataList: List[MetadataModel]): List[ResourceMetadataValue] = {
    val valueById = metadataList.filter(_.hasMetadataValue).flatMap{ metadata =>
      val ids = multiParams(metadata.name).filterNot(_.isEmpty).flatMap(v => toMetadataValueId(v)).toList
      MetadataValue.selectByIds(ids)
    }.map(mv => (mv.id, mv)).toMap
    val languageByCode = metadataList.filter(_.inputType == MetadataInputType.Language).flatMap{ metadata =>
      val codes = multiParams(metadata.name).map(_.split(':')(0)).filterNot(_.isEmpty).toList
      Language.selectByCodes(codes)
    }.map(l => (l.code, l)).toMap

    metadataList.flatMap{ metadata =>
      metadata.inputType match {
        case MetadataInputType.Text =>
          multiParams(metadata.name).filterNot(_.isEmpty).map(v =>
            ResourceMetadataValueText(metadata, v)
          )
        case MetadataInputType.TextArea =>
          multiParams(metadata.name).filterNot(_.isEmpty).map(v =>
            ResourceMetadataValueTextArea(metadata, v)
          )
        case MetadataInputType.Select =>
          val values = multiParams(metadata.name)
          val descs = multiParams(metadata.name + "-desc")
          values.zipWithIndex.filterNot{ case (value, index) =>
            value.isEmpty && descs(index).isEmpty
          }.map{ case (value, index) =>
              val valueOpt = toMetadataValueId(value).flatMap(id => valueById.get(id))
              ResourceMetadataValueSelect(metadata, valueOpt, descs(index))
          }
        case MetadataInputType.SelectOnly =>
          multiParams(metadata.name).filterNot(_.isEmpty).flatMap{ v =>
            toMetadataValueId(v).flatMap(id =>
              valueById.get(id).map(mv =>
                ResourceMetadataValueSelectOnly(metadata, mv)
              )
            )
          }
        case MetadataInputType.Relation =>
          val values = multiParams(metadata.name)
          val descs = multiParams(metadata.name + "-desc")
          values.zipWithIndex.filterNot{ case (value, index) =>
            value.isEmpty && descs(index).isEmpty
          }.map{ case (value, index) =>
            val valueOpt = toMetadataValueId(value).flatMap(id => valueById.get(id))
            ResourceMetadataValueRelation(metadata, valueOpt, descs(index))
          }
        case MetadataInputType.Language =>
          val values = multiParams(metadata.name)
          val descs = multiParams(metadata.name + "-desc")
          values.map(_.split(':')(0)).zipWithIndex.filterNot{ case (code, index) =>
            code.isEmpty && descs(index).isEmpty
          }.map{ case (code, index) =>
            ResourceMetadataValueLanguage(metadata, languageByCode.get(code), descs(index))
          }
        case MetadataInputType.Date =>
          val years = multiParams(metadata.name + "-year")
          val months = multiParams(metadata.name + "-month")
          val days = multiParams(metadata.name + "-day")
          val descs = multiParams(metadata.name + "-desc")
          years.zipWithIndex.filterNot{ case (year, index) =>
            year.isEmpty && descs(index).isEmpty
          }.map{ case (year, index) =>
            val date = years(index) + "-" + months(index) + "-" + days(index)
            ResourceMetadataValueDate(metadata, date, descs(index))
          }
        case MetadataInputType.Range =>
          val startYears = multiParams(metadata.name + "-startyear")
          val startMonths = multiParams(metadata.name + "-startmonth")
          val startDays = multiParams(metadata.name + "-startday")
          val endYears = multiParams(metadata.name + "-endyear")
          val endMonths = multiParams(metadata.name + "-endmonth")
          val endDays = multiParams(metadata.name + "-endday")
          val descs = multiParams(metadata.name + "-desc")
          startYears.zipWithIndex.filterNot{ case (v, index) =>
            startYears(index).isEmpty && endYears(index).isEmpty && descs(index).isEmpty
          }.map{ case (v, index) =>
            val startDate = startYears(index) + "-" + startMonths(index) + "-" + startDays(index)
            val endDate = endYears(index) + "-" + endMonths(index) + "-" + endDays(index)
            ResourceMetadataValueRange(metadata, startDate + " " + endDate, descs(index))
          }
      }
    }
  }
}
