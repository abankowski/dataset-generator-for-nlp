package com.evojam.nlp

import java.io.{ BufferedOutputStream, FileOutputStream }
import java.nio.charset.StandardCharsets

import scala.io.Source
import scala.util.Random

import com.evojam.nlp.model.{ Expression, DateTemplate }
import com.evojam.nlp.model.entity.{ Venue, Artist }
import com.evojam.nlp.model.{ Expression, DateTemplate }
import com.evojam.nlp.model.entity.{ Period, Determiner, Venue, Artist }

object Generator extends App {
  def filterNonAlphaChars(str: String) =
    str
      .replaceAll("[-_]", " ")
      .replaceAll("[^a-z0-9 ]", "")
      .replaceAll("\\s+", " ")

  lazy val artists = Source.fromFile("src/main/resources/artist.txt")
    .getLines().toList
    .map(_.toLowerCase)
    .map(filterNonAlphaChars)
    .filter(_.nonEmpty)
    .map(Artist)

  lazy val venues = Source.fromFile("src/main/resources/venues.txt")
    .getLines().toList
    .map(_.toLowerCase)
    .map(filterNonAlphaChars)
    .filter(_.nonEmpty)
    .map(Venue)

  lazy val determiners = Source.fromFile("src/main/resources/determiners.txt")
    .getLines().toList
    .map(_.toLowerCase)
    .map(filterNonAlphaChars)
    .filter(_.nonEmpty)
    .map(Determiner)

  lazy val periods = Source.fromFile("src/main/resources/periods.txt")
    .getLines().toList
    .map(_.toLowerCase)
    .map(filterNonAlphaChars)
    .filter(_.nonEmpty)
    .map(Period)

  lazy val dateTemplates = Source.fromFile("src/main/resources/date-template.txt")
    .getLines().toList
    .filter(_.nonEmpty)
    .map(DateTemplate)

  lazy val expressions = Source.fromFile("src/main/resources/expression.txt")
    .getLines().toList
    .filter(_.nonEmpty)
    .map(Expression)

  def pickSingle[T](list: List[T]): T = {
    require(list != null, "list cannot be null")
    require(list.nonEmpty, "list cannot be empty")

    list(Random.nextInt(list.size))
  }

  println(s"Artists: ${artists.size}, Venues: ${venues.size}, DateTemplates: ${dateTemplates.size}," +
          s" Expressions: ${expressions.size} Periods: ${periods.size} Determiners: ${determiners.size}")

  val trainingSetSize = 10000

  val out = new BufferedOutputStream(new FileOutputStream("out.train"))

  print("Working...")

  for (i <- 0 to trainingSetSize) {
    val (firstDate, secondDate) = pickSingle(dateTemplates).pickDates()
    val expressionBytes = pickSingle(expressions)
      .render(
        pickSingle(artists),
        pickSingle(venues),
        firstDate,
        secondDate,
        pickSingle(determiners),
        pickSingle(periods))
      .getBytes(StandardCharsets.UTF_8)

    out.write(expressionBytes)
    out.write("\n\n".getBytes(StandardCharsets.UTF_8))

    if(i % 100 == 0)
      print(s"\rWorking progress $i...")
  }

  println(".")
  println(s"Done training set size=$trainingSetSize")

  out.close()
}
