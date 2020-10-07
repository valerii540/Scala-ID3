package scalaID3.models.enums

/** Used by [[scalaID3.models.frames.standard.CommercialFrame]] */
object ReceivedAsTypes extends Enumeration {
  type ReceivedAs = Value

  val Other, StandardCD, CompressedCD, FileOverInternet, StreamOverInternet, AsNoteSheets, AsNoteSheetsInABook, MusicOnOtherMedia,
  NonMusicalMerchandise = Value
}
