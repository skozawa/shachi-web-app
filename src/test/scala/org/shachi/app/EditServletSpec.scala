import org.shachi.app.EditServlet
import org.scalatra.test.scalatest._
import org.shachi.test.ShachiSuite

class EditServletSpec extends ShachiSuite {
  addServlet(classOf[EditServlet], "/edit/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
