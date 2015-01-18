package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Annotator => AnnotatorModel}

object Annotator extends Schema {
  val annotator = table[AnnotatorModel]("annotator")

  on(annotator)(a => declare(
    a.name is(dbType("text")),
    a.mail is(dbType("text")),
    a.organization is(dbType("text"))
  ))

  def selectAll = from(annotator)(s => select(s))
}
