package scalaID3

import java.awt.image.BufferedImage
import java.io.{ByteArrayInputStream, File, RandomAccessFile}

import com.typesafe.scalalogging.LazyLogging
import javax.imageio.ImageIO
import scalaID3.Helper._
import scalaID3.models._
import scalaID3.models.enums.{ExtendedFlags, Flags}
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes.PictureType
import scalaID3.models.frames.FrameTypes.FrameType
import scalaID3.models.frames.{AttachedPictureFrame, Frame, FrameTypes, TextInfoFrame}

import scala.util.{Failure, Try}

sealed trait ID3TagReadOps {
  def getFrame(frameType: FrameType): Option[Frame]
  def getTextInfoFrame(frameType: FrameType): Option[TextInfoFrame]
  def getPictureFrame(pictureType: PictureType): Option[AttachedPictureFrame]
  def getPictureAsFile(path: String, pictureType: PictureType): File
  def prettyPrint(): Unit
}

sealed trait ID3TagWriteOps {
  def close(): Unit
}

final class ID3Tag(private val filePath: String) extends ID3TagWriteOps with ID3TagReadOps with LazyLogging {
  private implicit val file: RandomAccessFile = new RandomAccessFile(filePath, "r")

  val header: Header = parseHeader()

  private val framesWithPositions = FrameParser.traverseWholeFile(Map.empty.withDefaultValue(Nil))

  //TODO: is it needed?
  def getFrames: Seq[Frame] = framesWithPositions.values.flatten.map(_.frame).toSeq

  private def parseHeader(): Header = {
    val id        = file.take(3).map(_.toChar).mkString
    val version   = "v2." + file.take(2).mkString(".")
    val flagsByte = file.readByte()
    val sizeBytes = file.take(4)

    Try {
      assert(id == "ID3", s"Wrong ID3 identifier: $id")
      assert("v2\\.[3,4]\\.[0-9]+".r.matches(version), s"Unsupported ID3 version: $version")
    } match {
      case Failure(exception) =>
        file.close()
        throw exception
      case _ =>
    }

    val isExtended =
      if ((1 << 6 & flagsByte) == 0) {
        file.seek(10)
        false
      } else true

    val extendedHeaderSize = Option.when(isExtended)(file.readInt())
    val extendedFlags = Option.when(isExtended)(file.take(2)).map { bytes =>
      Set(Option.when((1 << 7 & bytes.head) != 0)(ExtendedFlags.HasCRC)).flatten
    }
    val sizeOfPadding = Option.when(isExtended)(file.readInt())
    val CRC32         = Option.when(isExtended && extendedFlags.get.contains(ExtendedFlags.HasCRC))(file.readInt())

    Header(
      id = id,
      version = version,
      flags = Set(
        Option.when((1 << 7 & flagsByte) != 0)(Flags.Unsynchronisation),
        Option.when(isExtended)(Flags.ExtendedHeader),
        Option.when((1 << 5 & flagsByte) != 0)(Flags.Experimental)
      ).flatten,
      size = sizeBytes(0) << 21 | sizeBytes(1) << 14 | sizeBytes(2) << 7 | sizeBytes(3),
      extendedHeaderSize = extendedHeaderSize,
      extendedFlags = extendedFlags,
      sizeOfPadding = sizeOfPadding,
      CRC32 = CRC32
    )
  }

  override def prettyPrint(): Unit =
    println(
      s"""
         |File: $filePath
         |
         |${header.prettySting}
         |${framesWithPositions.mkString("\n")}
         |""".stripMargin
    )

  override def getPictureFrame(pictureType: PictureType = PictureTypes.FrontCover): Option[AttachedPictureFrame] =
    framesWithPositions(FrameTypes.Picture.toString)
      .collectFirst { case FrameWithPosition(frame: AttachedPictureFrame, _) if frame.pictureType == pictureType => frame }

  override def getPictureAsFile(path: String, pictureType: PictureType = PictureTypes.FrontCover): File = {
    val pictureFrame = getPictureFrame(pictureType).getOrElse(throw new Exception(s"Picture with type $pictureType not found"))

    val mimeType = pictureFrame.mimeType.split("/").last

    assert(mimeType != "image", "Missing picture type in the frame")

    val bi: BufferedImage = ImageIO.read(new ByteArrayInputStream(pictureFrame.pictureData))
    val image             = new File(path.replaceFirst(removeExtensionRegex, "") + "." + mimeType)
    ImageIO.write(bi, mimeType, image)
    image
  }

  override def close(): Unit = file.close()

  override def getFrame(frameType: FrameType): Option[Frame] =
    framesWithPositions(frameType.toString).headOption.map(_.frame)

  override def getTextInfoFrame(frameType: FrameType): Option[TextInfoFrame] =
    framesWithPositions(frameType.toString).headOption
      .map(_.frame.as[TextInfoFrame])
}
