package scalaID3.models.frames

import java.nio.charset.{Charset, StandardCharsets}

import scalaID3.models.FrameHeader
import scalaID3.models.frames.TextInfoFrame.TextEncodings.Encoding

final case class TextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, description: Option[String], value: String) extends Frame {
  override def prettySting: String =
    s"""|--- TEXT INFO FRAME (${frameHeader.frameId}) ---
        |${frameHeader.prettySting}
        |----- BODY -----
        |encoding: $encoding
        |${description.map(_ => "description: " + _).getOrElse("")}
        |value: $value
        |""".stripMargin.replaceAll("\n\n", "\n")
}
object TextInfoFrame {
  object TextEncodings extends Enumeration {
    type Encoding = Value
    val `ISO-8859-1`: Value = Value("ISO-8859-1")
    val Unicode: Value      = Value("UTF-16")

    def identify(byte: Byte): Encoding =
      byte match {
        case 0 => TextInfoFrame.TextEncodings.`ISO-8859-1`
        case 1 => TextInfoFrame.TextEncodings.Unicode
        case x => throw new Exception(s"Unknown text frame encoding: $x")
      }

    def standardCharset(encoding: Encoding): Charset =
      encoding match {
        case `ISO-8859-1` => StandardCharsets.ISO_8859_1
        case Unicode      => StandardCharsets.UTF_16
      }
  }
}
