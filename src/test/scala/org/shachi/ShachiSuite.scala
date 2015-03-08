package org.shachi.test

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import org.shachi.db.DatabaseInit

trait ShachiSuite extends ScalatraSuite with FunSuiteLike {
}

trait ShachiSuiteDB extends ShachiSuite with DatabaseInit {
  def configureTestDb = {
    val jdbcurl = "jdbc:mysql://localhost:3306/shachi_test?useUnicode=true&characterEncoding=utf8"
    configureDb(jdbcurl, "root", "")
  }
}
