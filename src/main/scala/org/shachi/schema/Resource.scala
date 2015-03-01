package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import java.util.Date
import java.sql.Timestamp
import org.shachi.model.{Resource => ResourceModel}
import org.shachi.model.{ResourcesMetadata => ResourcesMetadataModel}
import org.shachi.model.{Metadata => MetadataModel}
import org.shachi.model.{AnnotatorId, ResourceId, MetadataId, LanguageId, ResourcesMetadataId}
import org.shachi.model.ResourceDetails._
import org.shachi.model.{MetadataInputType,ResourceStatus}

object Resource extends Schema {
  val resource = table[ResourceModel]("resource")

  on(resource)(r => declare(
    r.shachiId is(indexed("idx_shachi_id"), dbType("varchar(20)")),
    r.created is(indexed("idx_created")),
    columns(r.isPublic, r.created) are(indexed("idx_public_created"))
  ))

  def selectAll = from(resource)(r => select(r)).seq.toList

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
            Some(ResourceMetadataValueSelect(metadata, valueById.get(rm.valueId), rm.description.getOrElse("")))
          case MetadataInputType.SelectOnly =>
            valueById.get(rm.valueId).map(value =>
              ResourceMetadataValueSelectOnly(metadata, value)
            )
          case MetadataInputType.Relation =>
            Some(ResourceMetadataValueSelect(metadata, valueById.get(rm.valueId), rm.description.getOrElse("")))
          case MetadataInputType.Language =>
            Some(ResourceMetadataValueLanguage(metadata, languageByValueId.get(rm.valueId), rm.description.getOrElse("")))
          case MetadataInputType.Date =>
            Some(ResourceMetadataValueDate(metadata, rm.content.getOrElse(""), rm.description.getOrElse("")))
          case MetadataInputType.Range =>
            Some(ResourceMetadataValueRange(metadata, rm.content.getOrElse(""), rm.description.getOrElse("")))
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

  def create(
    title: String, annotatorId: AnnotatorId, values: List[ResourceMetadataValue],
    languageId: LanguageId = LanguageId(1819)
  ): ResourceModel = {
    val currentTime = new Timestamp((new Date).getTime)
    val newResource: ResourceModel = resource.insert(ResourceModel(
      ResourceId(0), shachiId = "", // dummy
      title = title, isPublic = true,
      annotatorId = annotatorId, status = ResourceStatus.New,
      created = currentTime, modified = currentTime
    ))

    val shachiId = shachiIdFromValues(newResource.id, values)
    updateShachiId(newResource.id, shachiId)

    val resourcesMetadata = values.map{v =>
      val item = v.toValueItem
      ResourcesMetadataModel(
        ResourcesMetadataId(0), // dummy
        newResource.id, v.metadata.id, languageId,
        item._1, item._2, item._3
      )
    }
    ResourcesMetadata.createMulti(resourcesMetadata)

    return newResource
  }

  private def shachiIdFromValues(resourceId: ResourceId, values: List[ResourceMetadataValue]): String = {
    val resourceSubjects = values.filter(_.metadata.name == "subject_resourceSubject")
    val prefix = {
      if (resourceSubjects.isEmpty) {
        'N'
      } else {
        resourceSubjects.head match {
          case rmv: ResourceMetadataValueSelect =>
            rmv.metadataValueOpt.fold('O'){ mv =>
              mv.value.toUpperCase.charAt(0)
            }
          case _ => 'O'
        }
      }
    }
    f"${prefix}%s-${resourceId.value}%06d"
  }

  def updateShachiId(resourceId: ResourceId, shachiId: String) =
    update(resource)(r =>
      where(r.id.value === resourceId.value)
      set(r.shachiId := shachiId)
    )

  def updateTitleAndAnnotator(id: ResourceId, title: String, annotatorId: AnnotatorId) =
    update(resource)(r =>
      where(r.id.value === id.value)
      set(r.title := title, r.annotatorId.value := annotatorId.value)
    )

  def updateModified(id: ResourceId) = {
    val currentTime = new Timestamp((new Date).getTime)
    update(resource)(r =>
      where(r.id.value === id.value)
      set(r.modified := currentTime)
    )
  }

  def updateResourceMetadata(
    resourceId: ResourceId, languageId: LanguageId = LanguageId(1819),
    metadataIds: List[MetadataId], newValues: List[ResourceMetadataValue]): Boolean = {
    val currentValues = ResourcesMetadata.selectEditMetadata(resourceId, languageId, metadataIds)
    val currentValuesByMetadataId = currentValues.groupBy(_.metadataId)
    val newValuesByMetadataId = newValues.groupBy(_.metadata.id)

    def _addAndDeleteItems(
      resourceId: ResourceId, metadataId: MetadataId, languageId: LanguageId,
      currentValues: List[ResourcesMetadataModel], newValues: List[ResourceMetadataValue]
    ): (List[ResourcesMetadataModel], List[ResourcesMetadataId]) = {
      val currentItems = currentValues.map(_.toValueItem)
      val newItems = newValues.map(_.toValueItem)

      val addItems = newItems.diff(currentItems)
      val addMetadataValues = addItems.map(item =>
        ResourcesMetadataModel(
          ResourcesMetadataId(0), // dummy
          resourceId, metadataId, languageId,
          item._1, item._2, item._3
        )
      )

      val delItems = currentItems.diff(newItems)
      val deleteMetadataValueIds = currentValues.filter{ v =>
        delItems.exists(_ == v.toValueItem)
      }.map(_.id)

      (addMetadataValues, deleteMetadataValueIds)
    }

    val addAndDeleteItems = metadataIds.map{ metadataId =>
      val currentValues = currentValuesByMetadataId.get(metadataId).getOrElse(List())
      val newValues = newValuesByMetadataId.get(metadataId).getOrElse(List())
      _addAndDeleteItems(resourceId, metadataId, languageId, currentValues, newValues)
    }

    val addResourcesMetadata = addAndDeleteItems.flatMap(_._1)
    val deleteMetadataValueIds = addAndDeleteItems.flatMap(_._2)

    ResourcesMetadata.createMulti(addResourcesMetadata)
    ResourcesMetadata.deleteByIds(deleteMetadataValueIds)

    addResourcesMetadata.nonEmpty || deleteMetadataValueIds.nonEmpty
  }
}
