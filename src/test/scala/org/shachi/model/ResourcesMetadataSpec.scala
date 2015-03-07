import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import scala.util.Random
import org.shachi.model.ResourcesMetadata
import org.shachi.model.{ResourcesMetadataId,ResourceId,MetadataId,LanguageId,MetadataValueId}

class ResourcesMetadataIdSpec extends ScalatraSuite with FunSuiteLike {
  test("ResourcesMetadata Id") {
    val idLong1 = Random.nextLong().abs
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

class ResourcesMetadataSpec extends ScalatraSuite with FunSuiteLike {
  test("ResourcesMetadata") {
    val rmId = ResourcesMetadataId(Random.nextLong().abs)
    val resourceId = ResourceId(Random.nextLong().abs)
    val metadataId = MetadataId(Random.nextLong().abs)
    val languageId = LanguageId(Random.nextLong().abs)
    val valueId = MetadataValueId(Random.nextLong.abs)
    val content = Some(Random.alphanumeric.take(10).mkString)
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
