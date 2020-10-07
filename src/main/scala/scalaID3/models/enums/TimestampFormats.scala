package scalaID3.models.enums

/** Used by [[scalaID3.models.frames.standard.SyncLyricsFrame]] */
object TimestampFormats extends Enumeration {
  type TimestampFormat = Value

  val MPEGBased: TimestampFormat   = Value(1)
  val MillisBased: TimestampFormat = Value(2)
}
