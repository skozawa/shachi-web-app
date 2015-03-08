import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite
import org.shachi.model.Language
import org.shachi.model.{LanguageId,MetadataValueId}

class LanguageIdSpec extends ShachiSuite {
  test("Language Id") {
    val idLong1 = randomLong
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
    val languageId = LanguageId(randomLong)
    val code = randomStr(3)
    val name = randomStr(10)
    val area = randomStr(10)
    val valueId = MetadataValueId(randomLong)
    val language = Language(languageId, code, name, area, valueId)

    language.id should be (languageId)
    language.code should be (code)
    language.name should be (name)
    language.area should be (area)
    language.valueId should be (valueId)
  }
}
