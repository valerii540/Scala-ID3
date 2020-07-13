package scalaID3.models.enums

object Flags extends Enumeration {
  type Flag = Value

  val Unsynchronisation, ExtendedHeader, Experimental = Value
}

object ExtendedFlags extends Enumeration {
  type ExtendedFlag = Value

  val HasCRC = Value
}

object FrameFlags extends Enumeration {
  type FrameFlag = Value

  val TagPreservation, FilePreservation, ReadOnly, Compression, Encryption, GroupingIdentity = Value
}
