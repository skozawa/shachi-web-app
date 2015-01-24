import org.shachi.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.shachi.db.DatabaseInit

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  override def init(context: ServletContext) {
    configureDb()
    context.mount(new ListServlet, "/list/*")
    context.mount(new FacetServlet, "/facet/*")
    context.mount(new StatisticsServlet, "/statistics/*")
    context.mount(new EditServlet, "/edit/*")
    context.mount(new ShachiServlet, "/*")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
