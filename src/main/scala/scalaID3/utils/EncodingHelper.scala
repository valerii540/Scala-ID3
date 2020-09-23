package scalaID3.utils

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.utils.EncodingHelper.TextEncodings.{Encoding, Unicode, `ISO-8859-1`}

import scala.util.Try

object EncodingHelper {
  object TextEncodings extends Enumeration {
    type Encoding = Value
    val `ISO-8859-1`: Encoding = Value("ISO-8859-1")
    val Unicode: Encoding      = Value("UTF-16")
  }

  def identify(byte: Byte): Try[Encoding] =
    Try(byte match {
      case 0 => `ISO-8859-1`
      case 1 => Unicode
      case x => throw new Exception(s"Unknown text frame encoding: $x")
    })

  def standardCharset(encoding: Encoding): Charset =
    encoding match {
      case `ISO-8859-1` => StandardCharsets.ISO_8859_1
      case Unicode      => StandardCharsets.UTF_16
    }
}