package com.evojam.nlp.model

import com.evojam.nlp.model.entity.{ Preposition, Date, Venue, Artist }

case class Expression(value: String) {
  require(value != null, "value cannot be null")
  require(value.nonEmpty, "value cannot be empty")

  def render(artist: Artist, venue: Venue, firstDate: Date, secondDate: Date): String =
    value.split("\\s+").foldLeft(List[String]()) {
      case (list, token) => token match {
        case "ARTIST" => list ::: artist.tokenizeAndTag
        case "VENUE" => list ::: venue.tokenizeAndTag
        case "FDATE" => list ::: firstDate.tokenizeAndTag
        case "SDATE" => list ::: secondDate.tokenizeAndTag
        case prep @ _ => list ::: Preposition(prep).tokenizeAndTag
      }
    }.mkString("\n")
}
