package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{MetadataValue => MetadataValueModel}
import org.shachi.model.{MetadataValueId}

object MetadataValue extends Schema {
  val metadataValue = table[MetadataValueModel]("metadata_value")

  on(metadataValue)(m => declare(
    m.valueType is(dbType("varchar(100)")),
    m.value is(dbType("varchar(100)")),
    columns(m.valueType, m.value) are(unique, indexed("idx_type_value"))
  ))

  def selectAll = from(metadataValue)(s => select(s))

  def selectById(id: MetadataValueId): Option[MetadataValueModel] =
    from(metadataValue)(mv => where(mv.id.value === id.value) select(mv)).headOption

  def selectByIds(ids: List[MetadataValueId]) =
    from(metadataValue)(mv => where(mv.id.value in ids.map(_.value)) select(mv)).seq.toList

  def selectExcludeLangauge =
    from(metadataValue)(mv => where(not(mv.valueType === "language")) select(mv)).seq.toList

  def create(model: MetadataValueModel) = metadataValue.insert(model)
}
