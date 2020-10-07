package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

sealed trait TextInfoFrame extends Frame {
  val encoding: Encoding
  val value: String
}

final case class StandardTextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, value: String) extends TextInfoFrame

final case class UserTextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, value: String, description: String) extends TextInfoFrame
