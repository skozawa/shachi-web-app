import org.scalatra.test.scalatest._
import scala.util.Random
import org.shachi.test.ShachiSuite
import org.shachi.model.Annotator
import org.shachi.model.AnnotatorId

class AnnotatorIdSpec extends ShachiSuite {
  test("Annotator Id") {
    val idLong1 = Random.nextLong().abs
    val idLong2 = Random.nextLong().abs

    val annotatorId1 = AnnotatorId(idLong1)
    val annotatorId2 = AnnotatorId(idLong2)
    val annotatorId3 = AnnotatorId(idLong1)

    annotatorId1.value should be (idLong1)
    annotatorId2.value should be (idLong2)
    annotatorId1 should not be (annotatorId2)
    annotatorId1 should be (annotatorId3)
  }
}

class AnnotatorSpec extends ShachiSuite {
  test("Annotator") {
    val annotatorId = AnnotatorId(Random.nextLong().abs)
    val name = Random.alphanumeric.take(10).mkString
    val mail = Random.alphanumeric.take(10).mkString + "@example.com"
    val org = Random.alphanumeric.take(10).mkString
    val annotator = Annotator(annotatorId, name, mail, org)

    annotator.id should be (annotatorId)
    annotator.name should be (name)
    annotator.mail should be (mail)
    annotator.organization should be (org)
  }
}
