package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

trait Domain[A] { self: CustomType[A] =>
  def validate(a: A): Unit
  def value: A

  validate(value)
}

class AnnotatorId(id: Int) extends IntField(id) with Domain[Int] {
  override def validate(id: Int) = assert(id > -1, "id must be positive, got " + id)
}

class Annotator(
  val id: AnnotatorId,
  val name: String,
  val mail: String,
  val organization: String
) extends KeyedEntity[IntField] {
}
