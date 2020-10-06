package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

final case class CommentFrame(frameHeader: FrameHeader, encoding: Encoding, language: String, description: String, comment: String) extends Frame
