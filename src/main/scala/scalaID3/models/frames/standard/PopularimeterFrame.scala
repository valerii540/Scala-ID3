package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

//TODO: is BigInt not too big for the counter?
final case class PopularimeterFrame(frameHeader: FrameHeader, email: String, rating: Int, counter: Option[BigInt]) extends Frame
