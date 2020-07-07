package scalaID3.models

object Flags extends Enumeration {
  type Flag = Value

  val Unsynchronisation, ExtendedHeader, Experimental = Value
}

object ExtendedFlags extends Enumeration {
  type ExtendedFlag = Value

  val HasCRC = Value
}
