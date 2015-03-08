import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite
import org.shachi.model.ResourcesMetadata
import org.shachi.model.{ResourcesMetadataId,ResourceId,MetadataId,LanguageId,MetadataValueId}

class ResourcesMetadataIdSpec extends ShachiSuite {
  test("ResourcesMetadata Id") {
    val idLong1 = randomLong
    val idLong2 = idLong1 + 1

    val rmId1 = ResourcesMetadataId(idLong1)
    val rmId2 = ResourcesMetadataId(idLong2)
    val rmId3 = ResourcesMetadataId(idLong1)

    rmId1.value should be (idLong1)
    rmId2.value should be (idLong2)
    rmId1 should not be (rmId2)
    rmId1 should be (rmId3)
  }
}

class ResourcesMetadataSpec extends ShachiSuite {
  test("ResourcesMetadata") {
    val rmId = ResourcesMetadataId(randomLong)
    val resourceId = ResourceId(randomLong)
    val metadataId = MetadataId(randomLong)
    val languageId = LanguageId(randomLong)
    val valueId = MetadataValueId(randomLong)
    val content = Some(randomStr(10))
    val description = None
    val rm = ResourcesMetadata(rmId, resourceId, metadataId, languageId, valueId, content, description)

    rm.id should be (rmId)
    rm.resourceId should be (resourceId)
    rm.metadataId should be (metadataId)
    rm.languageId should be (languageId)
    rm.valueId should be (valueId)
    rm.content should be (content)
    rm.description should be (description)
  }
}
