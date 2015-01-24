import sbt._
import Keys._
import org.scalatra.sbt._
import org.scalatra.sbt.PluginKeys._
import com.mojolly.scalate.ScalatePlugin._
import ScalateKeys._
import com.earldouglas.xsbtwebplugin.WebPlugin._
import com.earldouglas.xsbtwebplugin.PluginKeys._
import com.bowlingx.sbt.plugins.Wro4jPlugin._
import com.bowlingx.sbt.plugins.Wro4jPlugin.Wro4jKeys._

object ShachiWebAppBuild extends Build {
  val Organization = "org.shachi"
  val Name = "Shachi Web App"
  val Version = "0.1.0-SNAPSHOT"
  val ScalaVersion = "2.11.1"
  val ScalatraVersion = "2.3.0"

  lazy val project = Project (
    "shachi-web-app",
    file("."),
    settings = ScalatraPlugin.scalatraWithJRebel ++ scalateSettings ++ wro4jSettings ++ Seq(
      organization := Organization,
      name := Name,
      version := Version,
      scalaVersion := ScalaVersion,
      resolvers += Classpaths.typesafeReleases,
      libraryDependencies ++= Seq(
        "org.scalatra" %% "scalatra" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
        "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.2" % "runtime",
        "org.eclipse.jetty" % "jetty-webapp" % "9.1.5.v20140505" % "container",
        "org.eclipse.jetty" % "jetty-plus" % "9.1.5.v20140505" % "container",
        "javax.servlet" % "javax.servlet-api" % "3.1.0",
        // for DB
        "org.squeryl" %% "squeryl" % "0.9.5-7",
        "mysql" % "mysql-connector-java" % "5.1.10",
        "c3p0" % "c3p0" % "0.9.1.2"
      ),
      scalateTemplateConfig in Compile <<= (sourceDirectory in Compile){ base =>
        Seq(
          TemplateConfig(
            base / "webapp" / "WEB-INF" / "templates",
            Seq.empty,  /* default imports should be added here */
            Seq(
              Binding("context", "_root_.org.scalatra.scalate.ScalatraRenderContext", importMembers = true, isImplicit = true)
            ),  /* add extra bindings here */
            Some("templates")
          )
        )
      },
      (webappResources in Compile) <+= (targetFolder in generateResources in Compile)
    )
  )
}
