import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite
import org.shachi.model.Annotator
import org.shachi.model.AnnotatorId

class AnnotatorIdSpec extends ShachiSuite {
  test("Annotator Id") {
    val idLong1 = randomLong
    val idLong2 = randomLong

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
    val annotatorId = AnnotatorId(randomLong)
    val name = randomStr(10)
    val mail = randomStr(10) + "@example.com"
    val org = randomStr(10)
    val annotator = Annotator(annotatorId, name, mail, org)

    annotator.id should be (annotatorId)
    annotator.name should be (name)
    annotator.mail should be (mail)
    annotator.organization should be (org)
  }
}
