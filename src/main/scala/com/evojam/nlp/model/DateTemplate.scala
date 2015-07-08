package com.evojam.nlp.model

import scala.util.Random

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

import com.evojam.nlp.model.entity.Date

case class DateTemplate(value: String) {
  require(value != null, "value cannot be null")
  require(value.nonEmpty, "value cennot be empty")

  private val MinDate = new DateTime(1990, 1, 1, 0, 0)
  private val MinHours = 4000
  private val MaxHours = 260000

  def pickDates(): (Date, Date) = {
    val first = pickDate()
    val second = pickDate(first)

    (Date(format(first)), Date(format(second)))
  }

  private def format(date: DateTime): String =
    DateTimeFormat.forPattern(value).print(date).toLowerCase()

  private def pickDate(greaterThan: DateTime = MinDate): DateTime =
    greaterThan.plusHours(MinHours + Random.nextInt(MaxHours))
}
