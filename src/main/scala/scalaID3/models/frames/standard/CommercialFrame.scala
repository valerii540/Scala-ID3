package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.enums.ReceivedAsTypes.ReceivedAs
import scalaID3.models.enums.TextEncodings.Encoding
import scalaID3.models.frames.Frame

final case class CommercialFrame(frameHeader: FrameHeader,
                                 encoding: Encoding,
                                 price: String,
                                 validUntil: String,
                                 contactURL: String,
                                 receivedAs: ReceivedAs,
                                 seller: String,
                                 description: String,
                                 pictureMIME: String,
                                 sellerLogo: Array[Byte])
    extends Frame
