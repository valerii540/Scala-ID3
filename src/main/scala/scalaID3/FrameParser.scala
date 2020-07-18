package scalaID3

import java.io.RandomAccessFile

import scalaID3.Helper._
import scalaID3.models.enums.FrameTypes.FrameType
import scalaID3.models.enums.{FrameFlags, FrameTypes}
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.TextInfoFrame.TextEncodings
import scalaID3.models.frames.{AttachedPictureFrame, TextInfoFrame, UserTextInfoFrame}
import scalaID3.models.{FrameHeader, FrameWithPosition, frames}

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

private[scalaID3] object FrameParser {

  @tailrec
  def traverseFile(acc: Map[FrameType, Seq[FrameWithPosition]])(implicit file: RandomAccessFile): Map[FrameType, Seq[FrameWithPosition]] = {
    val framePosition = file.getFilePointer

    val frameHeaderOrNot = parseFrameHeader()

    if (frameHeaderOrNot.isRight) {
      val frameHeader = frameHeaderOrNot.right.get
      frameHeader.frameId.toString match {
        case id if id.head == 'T' =>
          val frame = if (id(1) != 'X') parseTextInfoFrame(frameHeader) else parseUserTextInfoFrame(frameHeader)
          traverseFile(acc + (frameHeader.frameId -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameId))))
        case id if id == "APIC" =>
          val frame = parseAttachedPictureFrame(frameHeader)
          traverseFile(acc + (frameHeader.frameId -> (FrameWithPosition(frame, framePosition) +: acc(frameHeader.frameId))))
        case x =>
          println(s"Unsupported frame ID type: $x")
          acc
      }
    } else {
      println(s"Unknown frame ID type: ${frameHeaderOrNot.left.get}")
      acc
    }

  }

  private def parseFrameHeader()(implicit file: RandomAccessFile): Either[String, FrameHeader] = {
    val frameID = file.take(4).map(_.toChar).mkString

    Try(FrameTypes.withName(frameID)) match {
      case Success(id) =>
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
            frameId = id,
            size = frameSize,
            flags = flags,
            decompressedSize = Option.when(flags.contains(FrameFlags.Compression))(file.readInt()),
            encryption = Option.when(flags.contains(FrameFlags.Encryption))(file.readByte()),
            group = Option.when(flags.contains(FrameFlags.GroupingIdentity))(file.readByte())
          ))
      case Failure(_) => Left(frameID)
    }
  }

  private def parseTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): TextInfoFrame = {
    val isUserDefined = frameHeader.frameId.toString.startsWith("TX")

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
