package org.shachi.test

import org.scalatra.test.scalatest._
import org.scalatest.FunSuiteLike
import org.shachi.db.DatabaseInit

trait ShachiSuite extends ScalatraSuite with FunSuiteLike {
}

trait ShachiSuiteDB extends ShachiSuite with DatabaseInit {
}
