package com.azavea.pgsockets4s.datamodel

import scala.util.Random

final case class City(
    id: Int,
    name: String,
    countryCode: String,
    district: String,
    population: Int
)

object City {
  private val someCountryCodes = List("PRT", "KEN", "CHN")

  def random: City = {
    val countryCode = Random.shuffle(someCountryCodes).head
    println(s"New city country code: $countryCode")
    City(
      Random.nextInt(1e6.toInt),
      Random.alphanumeric.take(6).mkString(""),
      countryCode,
      Random.alphanumeric.take(6).mkString(""),
      Random.nextInt(1e7.toInt)
    )
  }
}
