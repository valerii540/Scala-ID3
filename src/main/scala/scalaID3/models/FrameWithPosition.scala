package scalaID3.models

import scalaID3.models.frames.Frame

final case class FrameWithPosition(frame: Frame, position: Long)
