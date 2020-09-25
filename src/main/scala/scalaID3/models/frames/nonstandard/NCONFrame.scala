package scalaID3.models.frames.nonstandard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

/** Non-standard frame from MusicMatch software, [[https://id3.org/Compliance%20Issues]] */
final case class NCONFrame(frameHeader: FrameHeader, data: Array[Byte]) extends Frame
