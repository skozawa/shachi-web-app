package org.shachi.model

import org.squeryl.KeyedEntity
import org.squeryl.annotations.Column
import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._
import java.sql.Timestamp

case class ResourceId(id: Long) extends LongField(id) with EntityId[Resource] {
  override def validate(id: Long) = assert(id > -1, "id must be positive, got " + id)
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
case class Resource (
  val id: ResourceId,
  @Column("shachi_id") val shachiId: String,
  val title: String,
  @Column("is_public") val isPublic: Boolean,
  @Column("annotator_id") val annotatorId: AnnotatorId,
  @Column("status") val status: ResourceStatus,
  val created: Timestamp,
  val modified: Timestamp
) extends KeyedEntity[LongField] {
  def this() = this(ResourceId(1), "", "", true, AnnotatorId(1), ResourceStatus.New, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()))

  def editLink = "/edit/edit/" + id.value.toString
  def detailLink = "/edit/detail/" + id.value.toString
}

object ResourceDetails {
  case class ResourceDetail (
    resource: Resource,
    metadataValues: List[ResourceMetadataValue]
  ) {
    val valuesByMetadataId: Map[MetadataId, List[ResourceMetadataValue]] = metadataValues.groupBy(_.metadata.id)
    def getValuesByMetadataId(metadataId: MetadataId): List[ResourceMetadataValue] =
      valuesByMetadataId.getOrElse(metadataId, List())
  }

  sealed trait ResourceMetadataValue {
    def metadata: Metadata
    def toLabel: String
  }

  case class ResourceMetadataValueText (
    metadata: Metadata,
    content: String
  ) extends ResourceMetadataValue {
    def toLabel = content
  }

  case class ResourceMetadataValueTextArea (
    metadata: Metadata,
    content: String
  ) extends ResourceMetadataValue {
    def toLabel = content
  }

  case class ResourceMetadataValueSelect (
    metadata: Metadata,
    metadataValueOpt: Option[MetadataValue],
    comment: String
  ) extends ResourceMetadataValue {
    def toLabel = metadataValueOpt.fold("")(v => "[" + v.value + "]") + comment
  }

  case class ResourceMetadataValueSelectOnly (
    metadata: Metadata,
    metadataValue: MetadataValue
  ) extends ResourceMetadataValue {
    def toLabel = "[" + metadataValue.value + "]"
  }

  case class ResourceMetadataValueRelation (
    metadata: Metadata,
    metadataValueOpt: Option[MetadataValue],
    comment: String
  ) extends ResourceMetadataValue {
    def toLabel = metadataValueOpt.fold("")(v => "[" + v.value + "]") + comment
  }

  case class ResourceMetadataValueLanguage (
    metadata: Metadata,
    metadataValueOpt: Option[Language],
    comment: String
  ) extends ResourceMetadataValue {
    def toLabel = metadataValueOpt.fold("")(v => "[" + v.code + ":" + v.name + "]") + comment
    def editValue = metadataValueOpt.fold("")(v => v.code + ":" + v.name)
  }

  case class ResourceMetadataValueDate (
    metadata: Metadata,
    content: String,
    comment: String
  ) extends ResourceMetadataValue {
    def toLabel = "[" + content + "] " + comment
    lazy val ymd   = content.split('-') ++ List("", "") // avoid outofindex
    lazy val year  = ymd(0)
    lazy val month = ymd(1)
    lazy val day   = ymd(2)
  }

  case class ResourceMetadataValueRange (
    metadata: Metadata,
    content: String,
    comment: String
  ) extends ResourceMetadataValue {
    def toLabel = "[" + content + "] " + comment
    val ranges = content.split(' ') ++ List("") // avoid outofindex
    lazy val startymd   = ranges(0).split('-') ++ List("", "") // avoid outofindex
    lazy val startYear  = startymd(0)
    lazy val startMonth = startymd(1)
    lazy val startDay   = startymd(2)
    lazy val endymd     = ranges(1).split('-') ++ List("", "") // avoid outofindex
    lazy val endYear    = endymd(0)
    lazy val endMonth   = endymd(1)
    lazy val endDay     = endymd(2)
  }
}
