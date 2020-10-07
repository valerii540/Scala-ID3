package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

final case class EncapsulatedObjectFrame(frameHeader: FrameHeader,
                                   encoding: Encoding,
                                   mimeType: String,
                                   fileName: String,
                                   description: String,
                                   data: Array[Byte])
    extends Frame
