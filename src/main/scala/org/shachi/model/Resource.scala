package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._
import java.sql.Timestamp

class ResourceId(id: Int) extends IntField(id) with Domain[Int] {
  override def validate(id: Int) = assert(id > -1, "id must be positive, got " + id)
}

object ResourceStatus extends Enumeration {
  type ResourceStatus = Value
  val New = Value(1, "new")
  val Editing = Value(2, "editing")
  val Complete = Value(3, "complete")
  val Pending = Value(4, "pending")
  val Revised = Value(5, "revised")
  val Proofed = Value(6, "proofed")
  val Proofed2 = Value(7, "proofed2")
}

import ResourceStatus._
class Resource (
  val id: ResourceId,
  @Column("shachi_id") val shachiId: String,
  val title: String,
  @Column("is_public") val isPublic: Boolean,
  @Column("annotator_id") val annotatorId: AnnotatorId,
  @Column("status") val status: ResourceStatus,
  val created: Timestamp,
  val modified: Timestamp
) extends KeyedEntity[IntField] {
  def this() = this(new ResourceId(1), "", "", true, new AnnotatorId(1), ResourceStatus.New, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()))
}
