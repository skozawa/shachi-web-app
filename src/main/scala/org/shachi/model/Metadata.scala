package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

case class MetadataId(id: Long) extends LongField(id) with EntityId[Metadata] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
}

object MetadataInputType extends Enumeration {
  type MetadataInputType = Value
  val Text = Value(1, "text")
  val TextArea = Value(2, "textarea")
  val Select = Value(3, "select")
  val SelectOnly = Value(4, "select_only")
  val Relation = Value(5, "relation")
  val Language = Value(6, "language")
  val Date = Value(7, "date")
  val Range = Value(8, "range")
}

import MetadataInputType._
case class Metadata (
  val id: MetadataId,
  val name: String,
  val label: String,
  @Column("order_num") val orderNum: Int,
  val shown: Boolean,
  @Column("multi_value") val multiValue: Boolean,
  @Column("input_type") val inputType: MetadataInputType,
  @Column("value_type") val valueType: String,
  val color: String
) extends KeyedEntity[LongField] {
  def this() = this(MetadataId(1), "", "", 0, true, false, MetadataInputType.Text, "", "")

  def isOldSender: Boolean = name.matches("contributor_(author|speaker)_.*")

  def hasMetadataValue: Boolean = List(Select, SelectOnly, Relation, MetadataInputType.Language).exists(_ == inputType)
}
