import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import scala.util.Random
import java.sql.Timestamp
import org.shachi.model.Resource
import org.shachi.model.{ResourceId,AnnotatorId}
import org.shachi.model.ResourceStatus
import org.shachi.model.ResourceDetails

class ResourceIdSpec extends ScalatraSuite with FunSuiteLike {
  test("Resource Id") {
    val idLong1 = Random.nextLong().abs
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

class ResourceSpec extends ScalatraSuite with FunSuiteLike {
  test("Resource") {
    val resourceId = ResourceId(Random.nextLong().abs)
    val shachiId = Random.alphanumeric.take(10).mkString
    val title = Random.alphanumeric.take(10).mkString
    val isPublic = true
    val annotatorId = AnnotatorId(Random.nextLong().abs)
    val status = ResourceStatus.New
    val created = new Timestamp(System.currentTimeMillis)
    val modified = new Timestamp(System.currentTimeMillis)
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
