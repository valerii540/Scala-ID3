package scalaID3.models.frames

import scalaID3.models.FrameHeader

//TODO: is BigInt not too big for the counter?
final case class PopularimeterFrame(frameHeader: FrameHeader, email: String, rating: Int, counter: Option[BigInt]) extends Frame
