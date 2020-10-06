package scalaID3.models.enums

object TextEncodings extends Enumeration {
  type Encoding = Value

  val ISO_8859_1: Encoding = Value
  val UTF_16: Encoding     = Value
  val UTF_16BE: Encoding   = Value
  val UTF_8: Encoding      = Value
}
