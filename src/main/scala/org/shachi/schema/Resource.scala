package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Resource => ResourceModel}
import org.shachi.model.{ResourcesMetadata => ResourcesMetadataModel}
import org.shachi.model.{Metadata => MetadataModel}
import org.shachi.model.{AnnotatorId, ResourceId}
import org.shachi.model.ResourceDetails._
import org.shachi.model.MetadataInputType

object Resource extends Schema {
  val resource = table[ResourceModel]("resource")

  on(resource)(r => declare(
    r.shachiId is(indexed("idx_shachi_id"), dbType("varchar(20)")),
    r.created is(indexed("idx_created")),
    columns(r.isPublic, r.created) are(indexed("idx_public_created"))
  ))

  def selectAll = from(resource)(r => select(r))

  def selectById(resourceId: ResourceId): Option[ResourceModel] = from(resource)(r => where(r.id.value === resourceId.value) select(r)).headOption

  def selectDetailById(resourceId: ResourceId): Option[ResourceDetail] =
    from(resource)(r => where(r.id.value === resourceId.value) select(r))
      .headOption.map{resource: ResourceModel =>
      val resourceMetadata = ResourcesMetadata.selectByResourceId(resource.id)
      ResourceDetail(resource, selectResourceMetadaValues(resourceMetadata))
    }

  def selectResourceMetadaValues(rms: List[ResourcesMetadataModel]): List[ResourceMetadataValue] = {
    val metadataById = Metadata.selectByIds(rms.map(_.metadataId)).map(m => (m.id, m)).toMap
    val valueIds = rms.filter(_.valueId.value != 0).map(_.valueId)
    val valueById = MetadataValue.selectByIds(valueIds).map(mv => (mv.id, mv)).toMap
    val languageByValueId = Language.selectByValueIds(valueIds).map(l => (l.valueId, l)).toMap

    rms.flatMap { rm =>
      metadataById.get(rm.metadataId).flatMap { metadata: MetadataModel =>
        metadata.inputType match {
          case MetadataInputType.Text =>
            Some(ResourceMetadataValueText(metadata, rm.content.getOrElse("")))
          case MetadataInputType.TextArea =>
            Some(ResourceMetadataValueTextArea(metadata, rm.content.getOrElse("")))
          case MetadataInputType.Select =>
            valueById.get(rm.valueId).map(value =>
              ResourceMetadataValueSelect(metadata, value, rm.comment.getOrElse(""))
            )
          case MetadataInputType.SelectOnly =>
            valueById.get(rm.valueId).map(value =>
              ResourceMetadataValueSelectOnly(metadata, value)
            )
          case MetadataInputType.Relation =>
            valueById.get(rm.valueId).map(value =>
              ResourceMetadataValueSelect(metadata, value, rm.comment.getOrElse(""))
            )
          case MetadataInputType.Language =>
            languageByValueId.get(rm.valueId).map(language =>
              ResourceMetadataValueLanguage(metadata, language, rm.comment.getOrElse(""))
            )
          case MetadataInputType.Date =>
            Some(ResourceMetadataValueDate(metadata, rm.content.getOrElse(""), rm.comment.getOrElse("")))
          case MetadataInputType.Range =>
            Some(ResourceMetadataValueRange(metadata, rm.content.getOrElse(""), rm.comment.getOrElse("")))
          case _ => None
        }
      }
    }
  }

  def selectByAnnotatorId(annotatorId: AnnotatorId) =
    from(resource)(r => where(r.annotatorId.value === annotatorId.value) select(r)).seq.toList

  def countByAnnotatorId:Map[AnnotatorId, Int] = from(resource)(r =>
    groupBy(r.annotatorId.value) compute(count)
  ).map(c => (AnnotatorId(c.key), c.measures.toInt)).toMap
}
