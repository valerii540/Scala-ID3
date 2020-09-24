package scalaID3.models.frames

import scalaID3.models.FrameHeader

final case class MusicCDidFrame(frameHeader: FrameHeader, CDTableOfContents: Array[Byte]) extends Frame
