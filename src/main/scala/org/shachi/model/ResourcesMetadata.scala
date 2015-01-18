package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

class ResourcesMetadataId(id: Long) extends LongField(id) with Domain[Long] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

class ResourcesMetadata (
  val id: ResourcesMetadataId,
  @Column("resource_id") val resourceId: ResourceId,
  @Column("metadata_id") val metadataId: MetadataId,
  @Column("language_id") val languageId: LanguageId,
  @Column("value_id") val valueId: Int,
  val content: Option[String],
  val comment: Option[String]
) extends KeyedEntity[LongField] {
  def this() = this(new ResourcesMetadataId(1), new ResourceId(1), new MetadataId(1), new LanguageId(1), 0, None, None)
}








