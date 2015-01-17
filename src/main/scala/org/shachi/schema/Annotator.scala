package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.Annotator

object Annotators extends Schema {
  val annotators = table[Annotator]("annotators")

  def selectAll = from(annotators)(s => select(s))
}
