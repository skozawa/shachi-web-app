package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{MetadataValue => MetadataValueModel}

object MetadataValue extends Schema {
  val metadataValue = table[MetadataValueModel]("metadata_value")

  on(metadataValue)(m => declare(
    m.valueType is(dbType("varchar(100)")),
    m.value is(dbType("varchar(100)")),
    columns(m.valueType, m.value) are(unique, indexed("idx_type_value"))
  ))

  def selectAll = from(metadataValue)(s => select(s))
}
