package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Annotator => AnnotatorModel}
import org.shachi.model.AnnotatorId

object Annotator extends Schema {
  val annotator = table[AnnotatorModel]("annotator")

  on(annotator)(a => declare(
    a.name is(dbType("text")),
    a.mail is(dbType("text")),
    a.organization is(dbType("text"))
  ))

  def selectAll = from(annotator)(s => select(s))

  def selectById(id: AnnotatorId): Option[AnnotatorModel] =
    from(annotator)(a => where(a.id.value === id.value) select(a)).headOption

  def create(model: AnnotatorModel) = annotator.insert(model)
}
