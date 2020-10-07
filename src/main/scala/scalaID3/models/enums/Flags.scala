package scalaID3.models.enums

/** Used by [[scalaID3.models.ID3Header]] */
object Flags extends Enumeration {
  type Flag = Value

  val Unsynchronisation, ExtendedHeader, Experimental = Value
}

/** Used by [[scalaID3.models.ID3Header]] */
object ExtendedFlags extends Enumeration {
  type ExtendedFlag = Value

  val HasCRC: ExtendedFlag = Value
}

/** Used by [[scalaID3.models.FrameHeader]] */
object FrameFlags extends Enumeration {
  type FrameFlag = Value

  val TagPreservation, FilePreservation, ReadOnly, Compression, Encryption, GroupingIdentity = Value
}
