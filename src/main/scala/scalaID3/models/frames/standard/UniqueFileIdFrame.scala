package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

final case class UniqueFileIdFrame(frameHeader: FrameHeader, ownerId: String, id: Array[Byte]) extends Frame
