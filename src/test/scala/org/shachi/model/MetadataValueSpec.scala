import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import scala.util.Random
import org.shachi.model.MetadataValue
import org.shachi.model.MetadataValueId

class MetadataValueIdSpec extends ScalatraSuite with FunSuiteLike {
  test("MetadataValue Id") {
    val idLong1 = Random.nextLong().abs
    val idLong2 = Random.nextLong().abs

    val metadataValueId1 = MetadataValueId(idLong1)
    val metadataValueId2 = MetadataValueId(idLong2)
    val metadataValueId3 = MetadataValueId(idLong1)

    metadataValueId1.value should be (idLong1)
    metadataValueId2.value should be (idLong2)
    metadataValueId1 should not be (metadataValueId2)
    metadataValueId1 should be (metadataValueId3)
  }
}

class MetadataValueSpec extends ScalatraSuite with FunSuiteLike {
  test("MetadataValue") {
    val metadataValueId = MetadataValueId(Random.nextLong().abs)
    val valueType = Random.alphanumeric.take(10).mkString
    val value = Random.alphanumeric.take(10).mkString
    val metadataValue = MetadataValue(metadataValueId, valueType, value)

    metadataValue.id should be (metadataValueId)
    metadataValue.value should be (value)
    metadataValue.valueType should be (valueType)
  }
}
