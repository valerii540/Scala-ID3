package scalaID3.models.frames.standard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame
import scalaID3.models.frames.standard.ReceivedAsTypes.ReceivedAsType
import scalaID3.utils.EncodingHelper.TextEncodings.Encoding

final case class CommercialFrame(frameHeader: FrameHeader,
                                 encoding: Encoding,
                                 price: String,
                                 validUntil: String,
                                 contactURL: String,
                                 receivedAs: ReceivedAsType,
                                 seller: String,
                                 description: String,
                                 pictureMIME: String,
                                 sellerLogo: Array[Byte])
    extends Frame

object ReceivedAsTypes extends Enumeration {
  type ReceivedAsType = Value

  val Other: ReceivedAsType                 = Value(0)
  val StandardCD: ReceivedAsType            = Value(1)
  val CompressedCD: ReceivedAsType          = Value(2)
  val FileOverInternet: ReceivedAsType      = Value(3)
  val StreamOverInternet: ReceivedAsType    = Value(4)
  val AsNoteSheets: ReceivedAsType          = Value(5)
  val AsNoteSheetsInABook: ReceivedAsType   = Value(6)
  val MusicOnOtherMedia: ReceivedAsType     = Value(7)
  val NonMusicalMerchandise: ReceivedAsType = Value(8)
}
