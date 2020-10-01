package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

final case class AudioEncryptionFrame(frameHeader: FrameHeader,
                                      ownerId: String,
                                      previewStart: Short,
                                      previewLength: Short,
                                      encryptedInfo: Array[Byte])
    extends Frame
