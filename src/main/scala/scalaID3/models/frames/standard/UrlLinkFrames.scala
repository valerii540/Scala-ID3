package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

//TODO: Replace String with java.net.URL?
sealed trait UrlLinkFrame extends Frame {
  override val frameHeader: FrameHeader
  val url: String
}

final case class StandardUrlLinkFrame(frameHeader: FrameHeader, url: String) extends UrlLinkFrame

final case class UserDefinedUrlLinkFrame(frameHeader: FrameHeader, encoding: Encoding, description: String, url: String) extends UrlLinkFrame
