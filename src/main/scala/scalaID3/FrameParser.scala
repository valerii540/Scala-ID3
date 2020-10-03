package scalaID3

import java.io.RandomAccessFile

import scalaID3.data.AllFrameTypes
import scalaID3.models.enums.FrameFlags
import scalaID3.models.frames.nonstandard.NCONFrame
import scalaID3.models.frames.standard.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.standard._
import scalaID3.models.types._
import scalaID3.models.{FrameHeader, FrameWithPosition}
import scalaID3.utils.EncodingHelper
import scalaID3.utils.Helper._

import scala.annotation.tailrec

private[scalaID3] object FrameParser {

  @tailrec
  def traverseFile(acc: Map[FrameType, Seq[FrameWithPosition]])(implicit file: RandomAccessFile): Map[FrameType, Seq[FrameWithPosition]] = {
    val framePosition = file.getFilePointer

    val frameHeader = parseFrameHeader()

    frameHeader.frameType match {
      case _: TextInfoFrameType =>
        val frame = parseTextInfoFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case PictureFrameType =>
        val frame = parseAttachedPictureFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case CommentFrameType =>
        val frame = parseCommentFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case PrivateFrameType =>
        val frame = parsePrivateFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case MusicCDidFrameType =>
        val frame = parseMusicCDidFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case PopularimeterFrameType =>
        val frame = parsePopularimeterFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case AudioEncryptionFrameType =>
        val frame = parseAudioEncryptionFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case CommercialFrameType =>
        val frame = parseCommercialFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case NCONFrameType =>
        val frame = parseNCONFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case Unknown(id) =>
        println(s"From $framePosition unsupported frame ID type: $id")
        acc
    }

  }

  private def parseFrameHeader()(implicit file: RandomAccessFile): FrameHeader = {
    val frameID = file.take(4).map(_.toChar).mkString

    AllFrameTypes.matchFrameType(frameID) match {
      case unknown @ Unknown(_) =>
        FrameHeader(unknown, 0, Set(), None, None, None)

      case frameType: FrameType =>
        val frameSize       = file.readInt()
        val frameFlagsBytes = file.take(2)

        val flags = Set(
          Option.when((1 << 7 & frameFlagsBytes(0)) != 0)(FrameFlags.TagPreservation),
          Option.when((1 << 6 & frameFlagsBytes(0)) != 0)(FrameFlags.FilePreservation),
          Option.when((1 << 5 & frameFlagsBytes(0)) != 0)(FrameFlags.ReadOnly),
          Option.when((1 << 7 & frameFlagsBytes(1)) != 0)(FrameFlags.Compression),
          Option.when((1 << 6 & frameFlagsBytes(1)) != 0)(FrameFlags.Encryption),
          Option.when((1 << 5 & frameFlagsBytes(1)) != 0)(FrameFlags.GroupingIdentity)
        ).flatten

        FrameHeader(
          frameType = frameType,
          size = frameSize,
          flags = flags,
          decompressedSize = Option.when(flags.contains(FrameFlags.Compression))(file.readInt()),
          encryption = Option.when(flags.contains(FrameFlags.Encryption))(file.readByte()),
          group = Option.when(flags.contains(FrameFlags.GroupingIdentity))(file.readByte())
        )

    }
  }

  private def parseTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): TextInfoFrame = {
    val encoding = EncodingHelper.identify(file.readByte()).get

    frameHeader.frameType match {
      case TextInfoFrameTypes.UserDefinedText =>
        val descriptionBytes = file.takeWhile(_ != 0)
        val informationBytes = file.take(frameHeader.size - descriptionBytes.length - 2)

        UserTextInfoFrame(
          frameHeader = frameHeader,
          encoding = encoding,
          description = new String(descriptionBytes.toArray, EncodingHelper.standardCharset(encoding)),
          value = new String(informationBytes.toArray, EncodingHelper.standardCharset(encoding))
        )

      case _ =>
        val informationBytes = file.take(frameHeader.size - 1)

        StandardTextInfoFrame(
          frameHeader = frameHeader,
          encoding = encoding,
          value = new String(informationBytes.toArray, EncodingHelper.standardCharset(encoding))
        )
    }
  }

  private def parseCommentFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): CommentFrame = {
    val encoding         = EncodingHelper.identify(file.readByte()).get
    val language         = file.take(3).map(_.toChar).mkString
    val descriptionBytes = file.takeWhile(_ != 0)
    val commentBytes     = file.take(frameHeader.size - 4 - descriptionBytes.size - 1)

    CommentFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      language = language,
      description = new String(descriptionBytes.toArray, EncodingHelper.standardCharset(encoding)),
      comment = new String(commentBytes.toArray, EncodingHelper.standardCharset(encoding))
    )
  }

  private def parsePrivateFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): PrivateFrame = {
    val ownerIdBytes = file.takeWhile(_ != 0)
    val privateData  = file.take(frameHeader.size - ownerIdBytes.size - 1)

    PrivateFrame(
      frameHeader = frameHeader,
      ownerId = ownerIdBytes.map(_.toChar).mkString,
      privateData = privateData.toArray
    )
  }

  private def parseMusicCDidFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): MusicCDidFrame = {
    val CDTableOfContents = file.take(frameHeader.size)

    MusicCDidFrame(frameHeader = frameHeader, CDTableOfContents = CDTableOfContents.toArray)
  }

  private def parsePopularimeterFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): PopularimeterFrame = {
    val emailBytes = file.takeWhile(_ != 0)
    val rating     = file.readUnsignedByte()

    val counterSize = frameHeader.size - emailBytes.size - 4 - 1

    PopularimeterFrame(
      frameHeader = frameHeader,
      email = emailBytes.map(_.toChar).mkString,
      rating = rating,
      counter = Option.when(counterSize > 0)(BigInt(file.take(counterSize).toArray))
    )
  }

  private def parseAttachedPictureFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): AttachedPictureFrame = {
    val encoding    = EncodingHelper.identify(file.readByte()).get
    val mimeType    = file.takeWhile(_ != 0)
    val pictureType = file.readByte()
    val description = file.takeWhile(_ != 0)
    val pictureData = file.take(frameHeader.size - mimeType.size - description.size - 4).toArray

    AttachedPictureFrame(
      frameHeader = frameHeader,
      textEncoding = encoding,
      mimeType = mimeType.map(_.toChar).mkString,
      pictureType = PictureTypes(pictureType),
      description = new String(description.toArray, EncodingHelper.standardCharset(encoding)),
      pictureData = pictureData
    )
  }

  //FIXME: not tested
  private def parseAudioEncryptionFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): AudioEncryptionFrame = {
    val ownerIdBytes  = file.takeWhile(_ != 0)
    val previewStart  = file.readShort()
    val previewLength = file.readShort()
    val data          = file.take(frameHeader.size - ownerIdBytes.size - 4 - 1)

    AudioEncryptionFrame(
      frameHeader = frameHeader,
      ownerId = ownerIdBytes.map(_.toChar).mkString,
      previewStart = previewStart,
      previewLength = previewLength,
      encryptedInfo = data.toArray
    )
  }

  //FIXME: not tested
  private def parseCommercialFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): CommercialFrame = {
    val encoding         = EncodingHelper.identify(file.readByte()).get
    val priceBytes       = file.takeWhile(_ != 0)
    val validUntilBytes  = file.take(16)
    val contactURLBytes  = file.takeWhile(_ != 0)
    val receivedAs       = ReceivedAsTypes(file.readUnsignedByte())
    val sellerBytes      = file.takeWhile(_ != 0)
    val descriptionBytes = file.takeWhile(_ != 0)
    val mimeTypeBytes    = file.takeWhile(_ != 0)
    val logo =
      file.take(
        frameHeader.size - 1 - priceBytes.size - validUntilBytes.size - contactURLBytes.size - 1 - sellerBytes.size - descriptionBytes.size - mimeTypeBytes.size - 1
      )

    CommercialFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      price = priceBytes.map(_.toChar).mkString,
      validUntil = validUntilBytes.map(_.toChar).mkString,
      contactURL = contactURLBytes.map(_.toChar).mkString,
      receivedAs = receivedAs,
      seller = new String(sellerBytes.toArray, EncodingHelper.standardCharset(encoding)),
      description = new String(descriptionBytes.toArray, EncodingHelper.standardCharset(encoding)),
      pictureMIME = mimeTypeBytes.map(_.toChar).mkString,
      sellerLogo = logo.toArray
    )
  }

  private def parseNCONFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): NCONFrame = {
    val data = file.take(frameHeader.size)

    NCONFrame(frameHeader = frameHeader, data = data.toArray)
  }
}
