package scalaID3.utils

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.models.enums.TextEncodings
import scalaID3.models.enums.TextEncodings._

import scala.util.{Failure, Try}

object EncodingHelper {
  def identify(byte: Byte): Try[Encoding] =
    Try(TextEncodings(byte)).recoverWith { case _ => Failure(new Exception(s"Unknown text frame encoding: $byte")) }

  def standardCharset(encoding: Encoding): Charset =
    encoding match {
      case ISO_8859_1 => StandardCharsets.ISO_8859_1
      case UTF_16     => StandardCharsets.UTF_16
      case UTF_16BE   => StandardCharsets.UTF_16BE
      case UTF_8      => StandardCharsets.UTF_8
    }
}
