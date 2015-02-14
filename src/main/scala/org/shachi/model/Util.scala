package org.shachi.model

import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

trait EntityId[A] { self: CustomType[Long] =>
  def validate(a: Long): Unit
  def value: Long

  override def equals(other: Any): Boolean = other match {
    case that: AnnotatorId => this.isInstanceOf[AnnotatorId] && this.value == that.value
    case that: LanguageId  => this.isInstanceOf[LanguageId]  && this.value == that.value
    case that: MetadataId  => this.isInstanceOf[MetadataId]  && this.value == that.value
    case that: MetadataValueId => this.isInstanceOf[MetadataValueId] && this.value == that.value
    case that: ResourceId  => this.isInstanceOf[ResourceId]  && this.value == that.value
    case that: ResourcesMetadataId => this.isInstanceOf[ResourcesMetadataId] && this.value == that.value
    case _ => false
  }

  validate(value)
}

