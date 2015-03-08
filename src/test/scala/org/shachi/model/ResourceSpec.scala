import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite
import org.shachi.model.Resource
import org.shachi.model.{ResourceId,AnnotatorId}
import org.shachi.model.ResourceStatus
import org.shachi.model.ResourceDetails

class ResourceIdSpec extends ShachiSuite {
  test("Resource Id") {
    val idLong1 = randomLong
    val idLong2 = idLong1 + 1

    val resourceId1 = ResourceId(idLong1)
    val resourceId2 = ResourceId(idLong2)
    val resourceId3 = ResourceId(idLong1)

    resourceId1.value should be (idLong1)
    resourceId2.value should be (idLong2)
    resourceId1 should not be (resourceId2)
    resourceId1 should be (resourceId3)
  }
}

class ResourceSpec extends ShachiSuite {
  test("Resource") {
    val resourceId = ResourceId(randomLong)
    val shachiId = randomStr(10)
    val title = randomStr(10)
    val isPublic = true
    val annotatorId = AnnotatorId(randomLong)
    val status = ResourceStatus.New
    val created = currentTimestamp
    val modified = currentTimestamp
    val resource = Resource(
      resourceId, shachiId, title, isPublic, annotatorId,
      status, created, modified
    )

    resource.id should be (resourceId)
    resource.shachiId should be (shachiId)
    resource.title should be (title)
    resource.isPublic should be (isPublic)
    resource.annotatorId should be (annotatorId)
    resource.status should be (status)
    resource.created should be (created)
    resource.modified should be (modified)
  }
}
