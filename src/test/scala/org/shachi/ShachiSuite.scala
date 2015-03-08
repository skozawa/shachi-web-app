package org.shachi.test

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import org.shachi.db.DatabaseInit
import scala.util.Random
import java.sql.Timestamp

trait ShachiSuite extends ScalatraSuite with FunSuiteLike {
  def randomStr(num: Int): String = Random.alphanumeric.take(num).mkString
  def randomLong = Random.nextLong().abs
  def randomInt = Random.nextInt()
  def currentTimestamp = new Timestamp(System.currentTimeMillis)
}

trait ShachiSuiteDB extends ShachiSuite with DatabaseInit {
  import org.shachi.schema._
  import org.shachi.model.{MetadataInputType,ResourceStatus,ResourceDetails}

  def configureTestDb = {
    val jdbcurl = "jdbc:mysql://localhost:3306/shachi_test?useUnicode=true&characterEncoding=utf8"
    configureDb(jdbcurl, "root", "")
  }

  def createAnnotator (
    name: String = randomStr(10),
    mail: String = randomStr(10) + "@example.com",
    organization: String = randomStr(10)
  ) = {
    Annotator.create(org.shachi.model.Annotator(
      org.shachi.model.AnnotatorId(0), name, mail, organization
    ))
  }

  def createLanguage (
    code: String = randomStr(3),
    name: String = randomStr(10),
    area: String = randomStr(10),
    valueIdOpt: Option[org.shachi.model.MetadataValueId] = None
  ) = {
    val valueId = valueIdOpt.getOrElse(createMetadataValue().id)
    Language.create(org.shachi.model.Language(
      org.shachi.model.LanguageId(0), code, name, area, valueId
    ))
  }

  def createMetadata (
    name: String = randomStr(10),
    label: String = randomStr(10),
    orderNum: Int = Random.nextInt(),
    shown: Boolean = true,
    multiValue: Boolean = false,
    inputType: MetadataInputType.MetadataInputType = MetadataInputType.Text,
    valueType: String = randomStr(10),
    color: String = ""
  ) = {
    Metadata.create(org.shachi.model.Metadata(
      org.shachi.model.MetadataId(0), name, label, orderNum,
      shown, multiValue, inputType, valueType, color
    ))
  }

  def createMetadataValue (
    valueType: String = randomStr(10),
    value: String = randomStr(20)
  ) = {
    MetadataValue.create(org.shachi.model.MetadataValue(
      org.shachi.model.MetadataValueId(0), valueType, value
    ))
  }

  def createResource (
    title: String = randomStr(20),
    isPublic: Boolean = true,
    annotatorIdOpt: Option[org.shachi.model.AnnotatorId] = None,
    status: ResourceStatus.ResourceStatus = ResourceStatus.New,
    created: Timestamp = currentTimestamp,
    modified: Timestamp = currentTimestamp,
    values: List[ResourceDetails.ResourceMetadataValue] = List()
  ) = {
    val annotatorId = annotatorIdOpt.getOrElse(createAnnotator().id)
    Resource.create(title, annotatorId, values)
  }

  def createResourceMetadata (
    resourceIdOpt: Option[org.shachi.model.ResourceId] = None,
    metadataIdOpt: Option[org.shachi.model.MetadataId] = None,
    languageId: org.shachi.model.LanguageId = org.shachi.model.LanguageId(1819),
    valueIdOpt: Option[org.shachi.model.MetadataValueId] = None,
    content: Option[String] = None,
    description: Option[String] = None
  ) = {
    val resourceId = resourceIdOpt.getOrElse(createResource().id)
    val metadataId = metadataIdOpt.getOrElse(createMetadata().id)
    val valueId = valueIdOpt.getOrElse(org.shachi.model.MetadataValueId(0))
    ResourcesMetadata.create(org.shachi.model.ResourcesMetadata(
      org.shachi.model.ResourcesMetadataId(0), resourceId, metadataId,
      languageId, valueId, content, description
    ))
  }
}
