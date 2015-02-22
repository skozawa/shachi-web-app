package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Language => LanguageModel}

object Language extends Schema {
  val language = table[LanguageModel]("language")

  on(language)(l => declare(
    l.code is(unique, indexed("idx_code"), dbType("varchar(3)")),
    l.name is(dbType("varchar(100)")),
    l.area is(dbType("varchar(100)")),
    l.valueId.value is(unique, indexed("idx_value_id"))
  ))

  def selectAll = from(language)(s => select(s))
}
