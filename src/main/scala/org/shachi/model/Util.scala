package org.shachi.model

import org.squeryl.customtypes.CustomTypesMode._
import org.squeryl.customtypes._

trait Domain[A] { self: CustomType[A] =>
  def validate(a: A): Unit
  def value: A

  validate(value)
}
