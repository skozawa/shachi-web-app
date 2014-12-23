import org.shachi.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.shachi.db.DatabaseInit

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  override def init(context: ServletContext) {
    configureDb()
    context.mount(new ShachiServlet, "/*")
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
