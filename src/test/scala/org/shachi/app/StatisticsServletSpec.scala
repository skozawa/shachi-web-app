import org.shachi.app.StatisticsServlet
import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike

class StatisticsServletSpec extends ScalatraSuite with FunSuiteLike {
  addServlet(classOf[StatisticsServlet], "/statitics/*")

  test("simple get") {
    get("/") {
      status should equal(200)
    }
  }
}
