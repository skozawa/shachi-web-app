package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{ResourcesMetadata => ResourcesMetadataModel}
import org.shachi.model.{ResourcesMetadataId,ResourceId,MetadataId,LanguageId}

object ResourcesMetadata extends Schema {
  val resourcesMetadata = table[ResourcesMetadataModel]("resources_metadata")

  on(resourcesMetadata)(rm => declare(
    rm.resourceId.value is(indexed("idx_resource")),
    rm.content is(dbType("text")),
    rm.description is(dbType("text")),
    columns(rm.metadataId.value, rm.valueId.value) are(indexed("idx_metadata_value"))
  ))

  def selectAll = from(resourcesMetadata)(rm => select(rm))

  def selectByResourceId(resourceId: ResourceId) =
    from(resourcesMetadata)(rm => where(rm.resourceId.value === resourceId.value) select(rm)).seq.toList

  def selectEditMetadata(resourceId: ResourceId, languageId: LanguageId, metadataIds: List[MetadataId]) =
    from(resourcesMetadata)(rm => where(
      (rm.resourceId.value === resourceId.value) and
      (rm.languageId.value === languageId.value) and
      (rm.metadataId.value in metadataIds.map(_.value))
    ) select(rm) ).seq.toList

  def create(rm: ResourcesMetadataModel) = resourcesMetadata.insert(rm)

  def createMulti(rms: List[ResourcesMetadataModel]) = resourcesMetadata.insert(rms)

  def deleteByIds(ids: List[ResourcesMetadataId]) =
    resourcesMetadata.deleteWhere(rm => rm.id.value in ids.map(_.value))
}
