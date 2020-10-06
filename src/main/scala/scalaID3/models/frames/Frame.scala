package scalaID3.models.frames

import scalaID3.models.FrameHeader

//TODO: access modifier?
trait Frame {
  val frameHeader: FrameHeader
}
