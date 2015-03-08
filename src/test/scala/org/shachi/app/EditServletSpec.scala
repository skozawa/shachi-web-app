import org.shachi.app.EditServlet
import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuiteDB
import org.squeryl.PrimitiveTypeMode._

class EditServletSpec extends ShachiSuiteDB {
  configureTestDb
  addServlet(classOf[EditServlet], "/edit/*")

  test("simple get") {
    get("/edit/") {
      status should equal(200)
    }
  }
}
