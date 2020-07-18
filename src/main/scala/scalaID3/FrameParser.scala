package scalaID3

import java.io.RandomAccessFile

import scalaID3.Helper._
import scalaID3.models.enums.FrameFlags
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.TextInfoFrame.TextEncodings
import scalaID3.models.frames.{AttachedPictureFrame, TextInfoFrame, UserTextInfoFrame}
import scalaID3.models.{FrameHeader, FrameWithPosition, frames}

import scala.annotation.tailrec

private[scalaID3] object FrameParser {

  @tailrec
  def traverseWholeFile(acc: Map[String, Seq[FrameWithPosition]])(implicit file: RandomAccessFile): Map[String, Seq[FrameWithPosition]] = {
    val framePosition = file.getFilePointer

    val frameHeader = parseFrameHeader()

    frameHeader.frameId match {
      case id if id.head == 'T' =>
        val frame = if (id(1) != 'X') parseTextInfoFrame(frameHeader) else parseUserTextInfoFrame(frameHeader)
        traverseWholeFile(acc + (id -> (FrameWithPosition(frame, framePosition) +: acc(id))))
      case id if id == "APIC" =>
        val frame = parseAttachedPictureFrame(frameHeader)
        traverseWholeFile(acc + (id -> (FrameWithPosition(frame, framePosition) +: acc(id))))
      case x =>
        println(s"Unknown: $x")
        acc
    }
  }

  def parseFrameHeader()(implicit file: RandomAccessFile): FrameHeader = {
    val frameID         = file.take(4).map(_.toChar).mkString
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
      frameId = frameID,
      size = frameSize,
      flags = flags,
      decompressedSize = Option.when(flags.contains(FrameFlags.Compression))(file.readInt()),
      encryption = Option.when(flags.contains(FrameFlags.Encryption))(file.readByte()),
      group = Option.when(flags.contains(FrameFlags.GroupingIdentity))(file.readByte())
    )
  }

  def parseTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): TextInfoFrame = {
    val isUserDefined = frameHeader.frameId.startsWith("TX")

    val encoding         = TextEncodings.identify(file.readByte())
    val descriptionBytes = Option.when(isUserDefined)(file.takeWhile(_ != 0))
    val informationBytes = file.take(frameHeader.size - descriptionBytes.map(_.length).getOrElse(0) - 1)

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

  def parseUserTextInfoFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): UserTextInfoFrame =
    parseTextInfoFrame(frameHeader).as[UserTextInfoFrame]

  def parseAttachedPictureFrame(frameHeader: FrameHeader)(implicit file: RandomAccessFile): AttachedPictureFrame = {
    val encoding    = TextEncodings.identify(file.readByte())
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
