package scalaID3.models.frames

import scalaID3.models.FrameHeader
import scalaID3.utils.EncodingHelper.TextEncodings.Encoding

sealed trait TextInfoFrame extends Frame {
  override val frameHeader: FrameHeader
  val encoding: Encoding
  val value: String
}

final case class StandardTextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, value: String) extends TextInfoFrame

final case class UserTextInfoFrame(frameHeader: FrameHeader, encoding: Encoding, value: String, description: String) extends TextInfoFrame
