package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.PictureTypes.PictureType
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

final case class AttachedPictureFrame(frameHeader: FrameHeader,
                                      textEncoding: Encoding,
                                      mimeType: String,
                                      pictureType: PictureType,
                                      description: String,
                                      pictureData: Array[Byte])
    extends Frame
