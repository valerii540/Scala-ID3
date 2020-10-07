package scalaID3.models.enums

/** Used in frames that contain text data */
object TextEncodings extends Enumeration {
  type Encoding = Value

  val ISO_8859_1, UTF_16, UTF_16BE, UTF_8 = Value
}
