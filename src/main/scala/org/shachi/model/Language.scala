package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

class LanguageId(id: Int) extends IntField(id) with Domain[Int] {
  override def validate(id: Int) = assert(id > -1, "id must be positive, got " + id)
}

class Language(
  val id: LanguageId,
  val code: String,
  val name: String,
  val area: String
) extends KeyedEntity[IntField] {
}