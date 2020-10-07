package scalaID3.models.enums

/** Used by [[scalaID3.models.frames.standard.SyncLyricsFrame]] */
object ContentTypes extends Enumeration {
  type ContentType = Value

  val Other, Lyrics, TextTranscription, PartName, Events, Chord, Trivia, WebURLs, ImagesURLs = Value
}
