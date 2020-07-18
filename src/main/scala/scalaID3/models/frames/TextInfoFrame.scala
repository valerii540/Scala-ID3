package scalaID3.models.frames

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.models.FrameHeader
import scalaID3.models.frames.TextInfoFrame.TextEncodings.Encoding

import scala.util.Try

sealed case class TextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, value: String) extends Frame

final class UserTextInfoFrame(override val frameHeader: FrameHeader,
                              override val encoding: Encoding,
                              override val value: String,
                              val description: Option[String])
    extends TextInfoFrame(frameHeader, encoding, value)

//TODO: move to util package as EncodingHelper
object TextInfoFrame {
  object TextEncodings extends Enumeration {
    type Encoding = Value
    val `ISO-8859-1`: Value = Value("ISO-8859-1")
    val Unicode: Value      = Value("UTF-16")

    def identify(byte: Byte): Try[Encoding] =
      Try(byte match {
        case 0 => TextInfoFrame.TextEncodings.`ISO-8859-1`
        case 1 => TextInfoFrame.TextEncodings.Unicode
        case x => throw new Exception(s"Unknown text frame encoding: $x")
      })

    def standardCharset(encoding: Encoding): Charset =
      encoding match {
        case `ISO-8859-1` => StandardCharsets.ISO_8859_1
        case Unicode      => StandardCharsets.UTF_16
      }
  }
}
