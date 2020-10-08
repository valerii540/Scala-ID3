package scalaID3

import java.io.RandomAccessFile
import java.nio.charset.StandardCharsets

import scalaID3.models.enums.FrameTypes._
import scalaID3.models.enums._
import scalaID3.models.frames.nonstandard._
import scalaID3.models.frames.standard._
import scalaID3.models.frames.{Frame, UnknownFrame}
import scalaID3.models.{FrameHeader, FrameWithPosition}
import scalaID3.utils.EncodingHelper
import scalaID3.utils.Helper._

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

private[scalaID3] object FrameParser {

  @tailrec
  def traverseFile(acc: Map[FrameType, Seq[FrameWithPosition]])(implicit file: RandomAccessFile): Map[FrameType, Seq[FrameWithPosition]] = {
    val framePosition = file.getFilePointer

    val headerParsingAttempt = parseFrameHeader()

    headerParsingAttempt match {
      case Right(frameHeader) =>
        val frame: Frame = frameHeader.frameType match {
          // Standard frames
          case frameType if frameType.isTextInfoFrameType => parseTextInfoFrame(frameHeader)
          case frameType if frameType.isUrlLinkFrameType  => parseUrlLinkFrame(frameHeader)
          case Picture                                    => parseAttachedPictureFrame(frameHeader)
          case Comment                                    => parseCommentFrame(frameHeader)
          case Private                                    => parsePrivateFrame(frameHeader)
          case MusicCDid                                  => parseMusicCDidFrame(frameHeader)
          case Popularimeter                              => parsePopularimeterFrame(frameHeader)
          case UnsyncLyrics                               => parseUnsyncLyricsFrame(frameHeader)
          case SyncLyrics                                 => parseSyncLyricsFrame(frameHeader)
          case UniqueFileId                               => parseUniqueFileIdFrame(frameHeader)
          case EncapsulatedObject                         => parseEncapsulatedObjectFrame(frameHeader)
          case AudioEncryption                            => parseAudioEncryptionFrame(frameHeader)
          case Commercial                                 => parseCommercialFrame(frameHeader)
          // Non-standard frames
          case frameType if frameType.isITunesFrameType => parseITunesFrame(frameHeader)
          case MusicMatchNCON                           => parseNCONFrame(frameHeader)
        }
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case Left(unknownId) =>
        val frame = UnknownFrame(FrameHeader(Unknown, 0, Set(), None, None, None), unknownId, framePosition)
        acc + (Unknown -> (FrameWithPosition(frame, framePosition) +: acc(Unknown)))
    }
  }

  private def parseFrameHeader()(implicit file: RandomAccessFile): Either[String, FrameHeader] = {
    val frameID = file.take(4).map(_.toChar).mkString

    Try(FrameTypes.withName(frameID)) match {
      case Failure(_) => Left(frameID)

      case Success(frameType) =>
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

        Right(
          FrameHeader(
            frameType = frameType,
            size = frameSize,
            flags = flags,
            decompressedSize = Option.when(flags.contains(FrameFlags.Compression))(file.readInt()),
            encryption = Option.when(flags.contains(FrameFlags.Encryption))(file.readByte()),
            group = Option.when(flags.contains(FrameFlags.GroupingIdentity))(file.readByte())
          ))

    }
  }

  private def parseTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): TextInfoFrame = {
    val encoding = TextEncodings(file.readByte())

    frameHeader.frameType match {
      case UserDefinedText =>
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

  private def parseUrlLinkFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): UrlLinkFrame =
    frameHeader.frameType match {
      case UserDefinedLink =>
        val encoding         = TextEncodings(file.readByte())
        val descriptionBytes = file.takeWhile(_ != 0)
        val urlBytes         = file.take(frameHeader.size - 1 - descriptionBytes.size - 1)

        UserDefinedUrlLinkFrame(
          frameHeader = frameHeader,
          encoding = encoding,
          description = new String(descriptionBytes.toArray, EncodingHelper.standardCharset(encoding)),
          url = new String(urlBytes.toArray, StandardCharsets.ISO_8859_1)
        )

      case _ =>
        val urlBytes = file.take(frameHeader.size)

        StandardUrlLinkFrame(
          frameHeader = frameHeader,
          url = new String(urlBytes.toArray, StandardCharsets.ISO_8859_1)
        )
    }

  private def parseCommentFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): CommentFrame = {
    val encoding         = TextEncodings(file.readByte())
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
    val encoding    = TextEncodings(file.readByte())
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

  private def parseUnsyncLyricsFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): UnsyncLyricsFrame = {
    val encoding        = TextEncodings(file.readByte())
    val language        = file.take(3).map(_.toChar).mkString
    val descriptorBytes = file.takeWhile(_ != 0)
    val lyricsBytes     = file.take(frameHeader.size - 1 - 3 - descriptorBytes.size - 1)

    UnsyncLyricsFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      language = language,
      descriptor = new String(descriptorBytes.toArray, EncodingHelper.standardCharset(encoding)),
      lyrics = new String(lyricsBytes.toArray, EncodingHelper.standardCharset(encoding))
    )
  }

  private def parseSyncLyricsFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): SyncLyricsFrame = {
    val encoding = TextEncodings(file.readByte())
    val language = file.take(3).map(_.toChar).mkString
    val timestampFormat = Try(TimestampFormats(file.readByte())).recoverWith {
      case _ =>
        println("Unsupported timestamp format in the SyncLyricsFrame. Using MPEG based instead")
        Success(TimestampFormats.MPEGBased)
    }.get
    val contentType = ContentTypes(file.readByte())
    val descriptor  = new String(file.take(frameHeader.size - 6).toArray, EncodingHelper.standardCharset(encoding))

    SyncLyricsFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      language = language,
      timestampFormat = timestampFormat,
      contentType = contentType,
      descriptor = descriptor
    )
  }

  private def parseUniqueFileIdFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): UniqueFileIdFrame = {
    val ownerIdBytes = file.takeWhile(_ != 0)
    val id           = file.take(frameHeader.size - ownerIdBytes.size - 1)

    UniqueFileIdFrame(
      frameHeader = frameHeader,
      ownerId = ownerIdBytes.map(_.toChar).mkString,
      id = id.toArray
    )
  }

  private def parseEncapsulatedObjectFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): EncapsulatedObjectFrame = {
    val encoding         = TextEncodings(file.readByte())
    val mimeTypeBytes    = file.takeWhile(_ != 0)
    val fileNameBytes    = file.takeWhile(_ != 0)
    val descriptionBytes = file.takeWhile(_ != 0)
    val data             = file.take(frameHeader.size - 1 - mimeTypeBytes.size - 1 - fileNameBytes.size - 1 - descriptionBytes.size - 1)

    EncapsulatedObjectFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      mimeType = mimeTypeBytes.map(_.toChar).mkString,
      fileName = new String(fileNameBytes.toArray, EncodingHelper.standardCharset(encoding)),
      description = new String(descriptionBytes.toArray, EncodingHelper.standardCharset(encoding)),
      data = data.toArray
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
    val encoding         = TextEncodings(file.readByte())
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

  private def parseITunesFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): ITunesFrame = {
    val data = file.take(frameHeader.size)

    frameHeader.frameType match {
      case ITunesTitleSort         => ITunesTitleSortFrame(frameHeader, data.toArray)
      case ITunesArtistSort        => ITunesArtistSortFrame(frameHeader, data.toArray)
      case ITunesAlbumSort         => ITunesAlbumSortFrame(frameHeader, data.toArray)
      case ITunesAlbumArtistSort   => ITunesAlbumArtistSortFrame(frameHeader, data.toArray)
      case ITunesComposerSort      => ITunesComposerSortFrame(frameHeader, data.toArray)
      case ITunesPartOfCompilation => ITunesPartOfCompilationFrame(frameHeader, data.toArray)
    }
  }

  private def parseNCONFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): MusicMatchNCONFrame = {
    val data = file.take(frameHeader.size)

    MusicMatchNCONFrame(frameHeader = frameHeader, data = data.toArray)
  }
}
