package scalaID3.utils

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.models.enums.TextEncodings._

object EncodingHelper {
  def standardCharset(encoding: Encoding): Charset =
    encoding match {
      case ISO_8859_1 => StandardCharsets.ISO_8859_1
      case UTF_16     => StandardCharsets.UTF_16
      case UTF_16BE   => StandardCharsets.UTF_16BE
      case UTF_8      => StandardCharsets.UTF_8
    }
}
