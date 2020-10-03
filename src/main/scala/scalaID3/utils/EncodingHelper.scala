package scalaID3.utils

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.utils.EncodingHelper.TextEncodings.{Encoding, `ISO-8859-1`, `UTF-16BE`, `UTF-16`, `UTF-8`}

import scala.util.{Failure, Try}

object EncodingHelper {
  object TextEncodings extends Enumeration {
    type Encoding = Value

    val `ISO-8859-1`: Encoding = Value
    val `UTF-16`: Encoding     = Value
    val `UTF-16BE`: Encoding   = Value
    val `UTF-8`: Encoding      = Value
  }

  def identify(byte: Byte): Try[Encoding] =
    Try(TextEncodings(byte)).recoverWith { case _ => Failure(new Exception(s"Unknown text frame encoding: $byte")) }

  def standardCharset(encoding: Encoding): Charset =
    encoding match {
      case `ISO-8859-1` => StandardCharsets.ISO_8859_1
      case `UTF-16`     => StandardCharsets.UTF_16
      case `UTF-16BE`   => StandardCharsets.UTF_16BE
      case `UTF-8`      => StandardCharsets.UTF_8
    }
}
