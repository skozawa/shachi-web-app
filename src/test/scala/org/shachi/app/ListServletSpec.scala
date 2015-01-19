import org.shachi.app.ListServlet
import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class ListServletSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[ListServlet], "/list/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
