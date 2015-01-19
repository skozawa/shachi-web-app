import org.shachi.app.FacetServlet
import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class FacetServletSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[FacetServlet], "/facet/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
