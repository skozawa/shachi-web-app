package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

case class ResourcesMetadataId(id: Long) extends LongField(id) with EntityId[ResourcesMetadata] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

case class ResourcesMetadata (
  val id: ResourcesMetadataId,
  @Column("resource_id") val resourceId: ResourceId,
  @Column("metadata_id") val metadataId: MetadataId,
  @Column("language_id") val languageId: LanguageId,
  @Column("value_id") val valueId: MetadataValueId,
  val content: Option[String],
  val description: Option[String]
) extends KeyedEntity[LongField] {
  def this() = this(ResourcesMetadataId(1), ResourceId(1), MetadataId(1), LanguageId(1), MetadataValueId(1), None, None)

  def toValueItem = (valueId, content, description)
}

