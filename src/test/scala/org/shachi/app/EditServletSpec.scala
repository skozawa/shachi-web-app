import org.shachi.app.EditServlet
import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuiteDB

class EditServletSpec extends ShachiSuiteDB {
  addServlet(classOf[EditServlet], "/edit/*")
  configureTestDb

  test("simple get") {
    get("/edit/") {
      status should equal(200)
    }
  }
}
