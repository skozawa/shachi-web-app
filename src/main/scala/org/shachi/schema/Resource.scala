package org.shachi.schema

import org.squeryl.Schema
import org.squeryl.PrimitiveTypeMode._
import org.shachi.model.{Resource => ResourceModel}
import org.shachi.model.{AnnotatorId,ResourceId}

object Resource extends Schema {
  val resource = table[ResourceModel]("resource")

  on(resource)(r => declare(
    r.shachiId is(indexed("idx_shachi_id"), dbType("varchar(20)")),
    r.created is(indexed("idx_created")),
    columns(r.isPublic, r.created) are(indexed("idx_public_created"))
  ))

  def selectAll = from(resource)(r => select(r))

  def selectById(resourceId: ResourceId): Option[ResourceModel] = from(resource)(r => where(r.id.value === resourceId.value) select(r)).headOption

  def selectByAnnotatorId(annotatorId: AnnotatorId) = from(resource)(r => where(r.annotatorId.value === annotatorId.value) select(r))

  def countByAnnotatorId:Map[AnnotatorId, Int] = from(resource)(r =>
    groupBy(r.annotatorId.value) compute(count)
  ).map(c => (new AnnotatorId(c.key), c.measures.toInt)).toMap
}
