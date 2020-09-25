package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

final case class MusicCDidFrame(frameHeader: FrameHeader, CDTableOfContents: Array[Byte]) extends Frame
