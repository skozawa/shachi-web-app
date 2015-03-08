import org.scalatra.test.scalatest._
import scala.util.Random
import org.shachi.test.ShachiSuite
import org.shachi.model.Language
import org.shachi.model.{LanguageId,MetadataValueId}

class LanguageIdSpec extends ShachiSuite {
  test("Language Id") {
    val idLong1 = Random.nextLong().abs
    val idLong2 = idLong1 + 1

    val languageId1 = LanguageId(idLong1)
    val languageId2 = LanguageId(idLong2)
    val languageId3 = LanguageId(idLong1)

    languageId1.value should be (idLong1)
    languageId2.value should be (idLong2)
    languageId1 should not be (languageId2)
    languageId1 should be (languageId3)
  }
}

class LanguageSpec extends ShachiSuite {
  test("Language") {
    val languageId = LanguageId(Random.nextLong().abs)
    val code = Random.alphanumeric.take(3).mkString
    val name = Random.alphanumeric.take(10).mkString
    val area = Random.alphanumeric.take(10).mkString
    val valueId = MetadataValueId(Random.nextLong.abs)
    val language = Language(languageId, code, name, area, valueId)

    language.id should be (languageId)
    language.code should be (code)
    language.name should be (name)
    language.area should be (area)
    language.valueId should be (valueId)
  }
}
