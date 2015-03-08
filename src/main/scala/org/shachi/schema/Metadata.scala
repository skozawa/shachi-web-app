package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Metadata => MetadataModel}
import org.shachi.model.{MetadataId}

object Metadata extends Schema {
  val metadata = table[MetadataModel]("metadata")

  on(metadata)(m => declare(
    m.name is(unique, indexed("idx_name"), dbType("varchar(100)")),
    m.label is(dbType("varchar(100)")),
    m.valueType is(dbType("varchar(100)")),
    m.color is(dbType("varchar(20)")),
    columns(m.shown, m.orderNum) are(indexed("idx_shown_order"))
  ))

  def selectAll = from(metadata)(s => select(s))

  def selectShown = from(metadata)(m => where(m.shown === true) select(m)).seq.toList.sortBy(_.orderNum)

  def selectByIds(ids: List[MetadataId]) =
    from(metadata)(m => where(m.id.value in ids.map(_.value)) select(m)).seq.toList

  def create(model: MetadataModel) = metadata.insert(model)
}
