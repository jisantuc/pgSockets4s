package com.azavea.pgsockets4s.api

import eu.timepit.refined.types.string.NonEmptyString

package object implicits {

  implicit class combineNonEmptyString(s: NonEmptyString) {

    def +(otherString: String): NonEmptyString =
      NonEmptyString.unsafeFrom(s.value.concat(otherString))
  }

}
