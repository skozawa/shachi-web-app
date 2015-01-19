import org.shachi.app.ShachiServlet
import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class ShachiServletSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[ShachiServlet], "/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
