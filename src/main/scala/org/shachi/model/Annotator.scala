package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

class AnnotatorId(id: Long) extends LongField(id) with EntityId[Annotator] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

class Annotator(
  val id: AnnotatorId,
  val name: String,
  val mail: String,
  val organization: String
) extends KeyedEntity[LongField] {
}
