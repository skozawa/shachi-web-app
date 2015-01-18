package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Resource => ResourceModel}

object Resource extends Schema {
  val resource = table[ResourceModel]("resource")

  on(resource)(r => declare(
    r.shachiId is(indexed("idx_shachi_id"), dbType("varchar(20)")),
    r.created is(indexed("idx_created")),
    columns(r.isPublic, r.created) are(indexed("idx_public_created"))
  ))

  def selectAll = from(resource)(r => select(r)).page(0, 10)
}
