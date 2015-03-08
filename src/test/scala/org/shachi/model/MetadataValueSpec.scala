import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite
import org.shachi.model.MetadataValue
import org.shachi.model.MetadataValueId

class MetadataValueIdSpec extends ShachiSuite {
  test("MetadataValue Id") {
    val idLong1 = randomLong
    val idLong2 = randomLong

    val metadataValueId1 = MetadataValueId(idLong1)
    val metadataValueId2 = MetadataValueId(idLong2)
    val metadataValueId3 = MetadataValueId(idLong1)

    metadataValueId1.value should be (idLong1)
    metadataValueId2.value should be (idLong2)
    metadataValueId1 should not be (metadataValueId2)
    metadataValueId1 should be (metadataValueId3)
  }
}

class MetadataValueSpec extends ShachiSuite {
  test("MetadataValue") {
    val metadataValueId = MetadataValueId(randomLong)
    val valueType = randomStr(10)
    val value = randomStr(10)
    val metadataValue = MetadataValue(metadataValueId, valueType, value)

    metadataValue.id should be (metadataValueId)
    metadataValue.value should be (value)
    metadataValue.valueType should be (valueType)
  }
}
