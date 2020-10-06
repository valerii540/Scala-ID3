package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

final case class UnsyncLyricsFrame(frameHeader: FrameHeader, encoding: Encoding, language: String, descriptor: String, lyrics: String) extends Frame
