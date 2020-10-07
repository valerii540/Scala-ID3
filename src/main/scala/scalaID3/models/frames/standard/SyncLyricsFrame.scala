package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.ContentTypes.ContentType
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.enums.TimestampFormats.TimestampFormat
import scalaID3.models.frames.Frame

final case class SyncLyricsFrame(frameHeader: FrameHeader,
                                 encoding: Encoding,
                                 language: String,
                                 timestampFormat: TimestampFormat,
                                 contentType: ContentType,
                                 descriptor: String)
    extends Frame
