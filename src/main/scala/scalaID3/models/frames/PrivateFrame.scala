package scalaID3.models.frames

import scalaID3.models.FrameHeader

final case class PrivateFrame(frameHeader: FrameHeader, ownerId: String, privateData: Array[Byte]) extends Frame
