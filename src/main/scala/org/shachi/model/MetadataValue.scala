package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

class MetadataValueId(id: Long) extends LongField(id) with EntityId[MetadataValue] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

class MetadataValue (
  val id: MetadataValueId,
  @Column("value_type") val valueType: String,
  val value: String
) extends KeyedEntity[LongField] {
}
