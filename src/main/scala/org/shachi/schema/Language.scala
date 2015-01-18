package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.Language

object Languages extends Schema {
  val languages = table[Language]("languages")

  on(languages)(l => declare(
    l.code is(unique, indexed("idx_code"))
  ))

  def selectAll = from(languages)(s => select(s))
}
