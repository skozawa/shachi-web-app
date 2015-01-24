import org.shachi.app.EditServlet
import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class EditServletSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[EditServlet], "/edit/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
