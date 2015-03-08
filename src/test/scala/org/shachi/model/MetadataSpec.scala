import org.scalatra.test.scalatest._
import scala.util.Random
import org.shachi.test.ShachiSuite
import org.shachi.model.Metadata
import org.shachi.model.MetadataId
import org.shachi.model.MetadataInputType

class MetadataIdSpec extends ShachiSuite {
  test("Metadata Id") {
    val idLong1 = Random.nextLong().abs
    val idLong2 = idLong1 + 1

    val metadataId1 = MetadataId(idLong1)
    val metadataId2 = MetadataId(idLong2)
    val metadataId3 = MetadataId(idLong1)

    metadataId1.value should be (idLong1)
    metadataId2.value should be (idLong2)
    metadataId1 should not be (metadataId2)
    metadataId1 should be (metadataId3)
  }
}

class MetadataSpec extends ShachiSuite {
  test("Metadata") {
    val metadataId = MetadataId(Random.nextLong().abs)
    val name = Random.alphanumeric.take(10).mkString
    val label = Random.alphanumeric.take(10).mkString
    val orderNum = Random.nextInt()
    val shown = true
    val multiValue = false
    val inputType = MetadataInputType.Text
    val valueType = Random.alphanumeric.take(10).mkString
    val color = Random.alphanumeric.take(10).mkString
    val metadata = Metadata(
      metadataId, name, label, orderNum, shown,
      multiValue, inputType, valueType, color
    )

    metadata.id should be (metadataId)
    metadata.name should be (name)
    metadata.label should be (label)
    metadata.orderNum should be (orderNum)
    metadata.shown should be (shown)
    metadata.multiValue should be (multiValue)
    metadata.inputType should be (inputType)
    metadata.valueType should be (valueType)
    metadata.color should be (color)
  }

  test("idOldSender") {
    val default = Metadata(MetadataId(1), "", "", 0, true, false, MetadataInputType.Text, "", "")
    default.isOldSender should be (false)
    default.copy(name = "contributor_author_age").isOldSender should be (true)
    default.copy(name = "contributor_age").isOldSender should be(false)
    default.copy(name = "contributor_speaker_gender").isOldSender should be (true)
  }

  test("hasMetadataValue") {
    val default = Metadata(MetadataId(1), "", "", 0, true, false, MetadataInputType.Text, "", "")
    default.copy(inputType = MetadataInputType.Text).hasMetadataValue should be (false)
    default.copy(inputType = MetadataInputType.TextArea).hasMetadataValue should be (false)
    default.copy(inputType = MetadataInputType.Select).hasMetadataValue should be (true)
    default.copy(inputType = MetadataInputType.SelectOnly).hasMetadataValue should be (true)
    default.copy(inputType = MetadataInputType.Relation).hasMetadataValue should be (true)
    default.copy(inputType = MetadataInputType.Language).hasMetadataValue should be (true)
    default.copy(inputType = MetadataInputType.Date).hasMetadataValue should be (false)
    default.copy(inputType = MetadataInputType.Range).hasMetadataValue should be (false)
  }
}



