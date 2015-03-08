package org.shachi.db

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.squeryl.adapters.MySQLAdapter
import org.squeryl.Session
import org.squeryl.SessionFactory
import org.slf4j.LoggerFactory

trait DatabaseInit {
  val logger = LoggerFactory.getLogger(getClass)

  var cpds = new ComboPooledDataSource

  def configureDb(jdbcUrl: String, user: String, pass: String) {
    cpds.setDriverClass("com.mysql.jdbc.Driver")
    cpds.setJdbcUrl(jdbcUrl)
    cpds.setUser(user)
    cpds.setPassword(pass)

    cpds.setMinPoolSize(1)
    cpds.setAcquireIncrement(1)
    cpds.setMaxPoolSize(50)

    SessionFactory.concreteFactory = Some(() => connection)

    def connection = {
      logger.info("Creating connection with c3po connection pool")
      Session.create(cpds.getConnection, new MySQLAdapter)
    }
  }

  def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close()
  }
}
