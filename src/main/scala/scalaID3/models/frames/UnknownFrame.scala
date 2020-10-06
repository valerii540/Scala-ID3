package scalaID3.models.frames

import scalaID3.models.FrameHeader

case class UnknownFrame(frameHeader: FrameHeader) extends Frame
