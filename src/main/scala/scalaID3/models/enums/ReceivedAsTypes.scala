package scalaID3.models.enums

object ReceivedAsTypes extends Enumeration {
  type ReceivedAs = Value

  val Other, StandardCD, CompressedCD, FileOverInternet, StreamOverInternet, AsNoteSheets, AsNoteSheetsInABook, MusicOnOtherMedia,
  NonMusicalMerchandise = Value
}
