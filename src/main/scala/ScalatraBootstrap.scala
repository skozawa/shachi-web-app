import org.shachi.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.shachi.db.DatabaseInit

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  override def init(context: ServletContext) {
    configureDb()
    context.mount(new List, "/list/*")
    context.mount(new Facet, "/facet/*")
    context.mount(new Statistics, "/statistics/*")
    context.mount(new ShachiServlet, "/*")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
