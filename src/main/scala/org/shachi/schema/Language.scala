package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Language => LanguageModel}
import org.shachi.model.{MetadataValueId}

object Language extends Schema {
  val language = table[LanguageModel]("language")

  on(language)(l => declare(
    l.code is(unique, indexed("idx_code"), dbType("varchar(3)")),
    l.name is(dbType("varchar(100)")),
    l.area is(dbType("varchar(100)")),
    l.valueId.value is(unique, indexed("idx_value_id"))
  ))

  def selectAll = from(language)(s => select(s))

  def selectByValueId(id: MetadataValueId): Option[LanguageModel] =
    from(language)(l => where(l.valueId.value === id.value) select(l)).headOption

  def selectByValueIds(ids: List[MetadataValueId]) =
    from(language)(l => where(l.valueId.value in ids.map(_.value)) select(l)).seq.toList

  def selectByCodes(codes: List[String]) =
    from(language)(l => where(l.code in codes) select(l)).seq.toList

  def create(model: LanguageModel) = language.insert(model)
}
