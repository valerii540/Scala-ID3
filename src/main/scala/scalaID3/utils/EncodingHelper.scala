package scalaID3.utils

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.utils.EncodingHelper.TextEncodings.{Encoding, `ISO-8859-1`, `UTF-16`, `UTF-16BE`, `UTF-8`}

import scala.util.Try

object EncodingHelper {
  object TextEncodings extends Enumeration {
    type Encoding = Value
    val `ISO-8859-1`: Encoding = Value("ISO-8859-1")
    val `UTF-16`: Encoding     = Value("UTF-16")
    val `UTF-16BE`: Encoding   = Value("UTF-16BE")
    val `UTF-8`: Encoding      = Value("UTF-8")
  }

  def identify(byte: Byte): Try[Encoding] =
    Try(byte match {
      case 0 => `ISO-8859-1`
      case 1 => `UTF-16`
      case 2 => `UTF-16BE`
      case 3 => `UTF-8`
      case x => throw new Exception(s"Unknown text frame encoding: $x")
    })

  def standardCharset(encoding: Encoding): Charset =
    encoding match {
      case `ISO-8859-1` => StandardCharsets.ISO_8859_1
      case `UTF-16`     => StandardCharsets.UTF_16
      case `UTF-16BE`   => StandardCharsets.UTF_16BE
      case `UTF-8`      => StandardCharsets.UTF_8
    }
}
