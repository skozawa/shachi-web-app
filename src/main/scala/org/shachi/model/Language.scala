package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

class LanguageId(id: Long) extends LongField(id) with EntityId[Language] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

class Language(
  val id: LanguageId,
  val code: String,
  val name: String,
  val area: String,
  @Column("value_id") val valueId: MetadataValueId
) extends KeyedEntity[LongField] {
  def this() = this(new LanguageId(1), "", "", "", new MetadataValueId(1))
}
