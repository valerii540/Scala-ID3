package scalaID3.models.frames

import scalaID3.models.FrameHeader
import scalaID3.utils.EncodingHelper.TextEncodings.Encoding

final case class CommentFrame(frameHeader: FrameHeader, encoding: Encoding, language: String, description: String, comment: String) extends Frame
