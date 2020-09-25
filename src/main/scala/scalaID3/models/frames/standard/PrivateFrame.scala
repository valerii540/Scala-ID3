package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

final case class PrivateFrame(frameHeader: FrameHeader, ownerId: String, privateData: Array[Byte]) extends Frame
