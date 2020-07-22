package scalaID3

import java.io.RandomAccessFile

import scalaID3.Helper._
import scalaID3.data.AllFrameTypes
import scalaID3.models.enums.FrameFlags
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.TextInfoFrame.TextEncodings
import scalaID3.models.frames.{AttachedPictureFrame, CommentFrame, TextInfoFrame, UserTextInfoFrame}
import scalaID3.models.types.TextInfoFrameTypes.UserDefinedText
import scalaID3.models.types._
import scalaID3.models.{FrameHeader, FrameWithPosition, frames}

import scala.annotation.tailrec

private[scalaID3] object FrameParser {

  @tailrec
  def traverseFile(acc: Map[FrameType, Seq[FrameWithPosition]])(implicit file: RandomAccessFile): Map[FrameType, Seq[FrameWithPosition]] = {
    val framePosition = file.getFilePointer

    val frameHeader = parseFrameHeader()

    frameHeader.frameType match {
      case t: TextInfoFrameType =>
        val frame = if (t.id != UserDefinedText.id) parseTextInfoFrame(frameHeader) else parseUserTextInfoFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case PictureFrameType =>
        val frame = parseAttachedPictureFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case CommentFrameType =>
        val frame = parseCommentFrame(frameHeader)
        traverseFile(acc + (frameHeader.frameType -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameType))))

      case Unknown(id) =>
        println(s"Unsupported frame ID type: $id")
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
    val isUserDefined = frameHeader.frameType.toString.startsWith("TX")

    val encoding         = TextEncodings.identify(file.readByte()).get
    val descriptionBytes = Option.when(isUserDefined)(file.takeWhile(_ != 0))
    val informationBytes = file.take(frameHeader.size - descriptionBytes.map(_.length + 1).getOrElse(0) - 1)

    if (isUserDefined)
      new UserTextInfoFrame(
        frameHeader = frameHeader,
        encoding = encoding,
        description = descriptionBytes.map(s => new String(s.toArray, TextEncodings.standardCharset(encoding))),
        value = new String(informationBytes.toArray, TextEncodings.standardCharset(encoding))
      )
    else
      TextInfoFrame(
        frameHeader = frameHeader,
        encoding = encoding,
        value = new String(informationBytes.toArray, TextEncodings.standardCharset(encoding))
      )
  }

  private def parseUserTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): UserTextInfoFrame =
    parseTextInfoFrame(frameHeader).as[UserTextInfoFrame]

  private def parseCommentFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): CommentFrame = {
    val encoding         = TextEncodings.identify(file.readByte()).get
    val language         = file.take(3).map(_.toChar).mkString
    val descriptionBytes = file.takeWhile(_ != 0)
    val commentBytes     = file.take(frameHeader.size - 4 - descriptionBytes.size)

    CommentFrame(
      frameHeader = frameHeader,
      encoding = encoding,
      language = language,
      description = new String(descriptionBytes.toArray, TextEncodings.standardCharset(encoding)),
      comment = new String(commentBytes.toArray, TextEncodings.standardCharset(encoding))
    )
  }

  private def parseAttachedPictureFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): AttachedPictureFrame = {
    val encoding    = TextEncodings.identify(file.readByte()).get
    val mimeType    = file.takeWhile(_ != 0)
    val pictureType = file.readByte()
    val description = file.takeWhile(_ != 0)
    val pictureData = file.take(frameHeader.size - mimeType.length - description.length - 2).toArray

    frames.AttachedPictureFrame(
      frameHeader = frameHeader,
      textEncoding = encoding,
      mimeType = mimeType.map(_.toChar).mkString,
      pictureType = PictureTypes(pictureType),
      description = new String(description.toArray, TextEncodings.standardCharset(encoding)),
      pictureData = pictureData
    )
  }

}
