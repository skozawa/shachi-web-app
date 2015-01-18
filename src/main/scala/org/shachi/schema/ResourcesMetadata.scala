package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{ResourcesMetadata => ResourcesMetadataModel}

object ResourcesMetadata extends Schema {
  val resourcesMetadata = table[ResourcesMetadataModel]("resources_metadata")

  on(resourcesMetadata)(rm => declare(
    rm.resourceId.value is(indexed("idx_resource")),
    rm.content is(dbType("text")),
    rm.comment is(dbType("text")),
    columns(rm.metadataId.value, rm.valueId) are(indexed("idx_metadata_value"))
  ))

  def selectAll = from(resourcesMetadata)(rm => select(rm))
}
