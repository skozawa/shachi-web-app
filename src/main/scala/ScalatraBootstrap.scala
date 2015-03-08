import org.shachi.app._
import org.scalatra._
import javax.servlet.ServletContext
import org.shachi.db.DatabaseInit

class ScalatraBootstrap extends LifeCycle with DatabaseInit {
  override def init(context: ServletContext) {
    configure(context)
    configureDb(
      context.getInitParameter("org.scalatra.jdbcurl"),
      context.getInitParameter("org.scalatra.dbuser"),
      context.getInitParameter("org.scalatra.dbpass")
    )
    context.mount(new ListServlet, "/list/*")
    context.mount(new FacetServlet, "/facet/*")
    context.mount(new StatisticsServlet, "/statistics/*")
    context.mount(new EditServlet, "/edit/*")
    context.mount(new ShachiServlet, "/*")
  }

  private val configs: Map[String, Map[String, String]] = {
    Map(
      "production" -> Map(
      ),
      "development" -> Map(
        "org.scalatra.jdbcurl" -> "jdbc:mysql://localhost:3306/shachi?useUnicode=true&characterEncoding=utf8",
        "org.scalatra.dbuser" -> "root",
        "org.scalatra.dbpass" -> ""
      )
    )
  }

  private def configure(context: ServletContext) {
    val env = context.environment.toLowerCase
    val currentConfig = configs.getOrElse(env, Map())
    val devConfig = configs.getOrElse("development", Map())

    val keys = (currentConfig.keys ++ devConfig.keys).toList.distinct
    keys.foreach{ key =>
      val value = currentConfig.getOrElse(key, devConfig.getOrElse(key, ""))
      context.setInitParameter(key, value)
    }
  }

  override def destroy(context:ServletContext) {
    closeDbConnection()
  }
}
