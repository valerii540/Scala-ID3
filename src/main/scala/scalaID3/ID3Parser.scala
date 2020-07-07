package scalaID3

import java.io.RandomAccessFile

import scalaID3.models.{ExtendedFlags, ExtendedHeader, Flags, Header}

import scala.util.{Failure, Try}

sealed trait ParserOps {
  def close(): Unit

  def prettyPrint(): Unit
}

final class ID3Parser(private val filePath: String) extends ParserOps {
  private[this] val file = new RandomAccessFile(filePath, "r")

  val header: Header = parseHeader

  Try {
    assert(header.id == "ID3", s"Wrong ID3 identifier: ${header.id}")
    assert("v2\\.[3,4]\\.[0-9]+".r.matches(header.version), s"Unsupported ID3 version: ${header.version}")
  } match {
    case Failure(exception) =>
      close()
      throw exception
    case _ =>
  }

  val extendedHeader: Option[ExtendedHeader] = Option.when(header.flags.contains(Flags.ExtendedHeader))(parseExtendedHeader)

  private[this] def parseHeader: Header = {
    file.seek(0)

    val headerBytes = new Array[Byte](10)
    file.read(headerBytes)

    val idBytes = headerBytes.take(3)
    val versionBytes = headerBytes.slice(3, 5)
    val flagsByte = headerBytes(5)
    val sizeBytes = headerBytes.drop(6)

    Header(
      id = idBytes.map(_.toChar).mkString,
      version = "v2." + versionBytes.mkString("."),
      flags = Seq(
        Option.when((1 << 7 & flagsByte) != 0)(Flags.Unsynchronisation),
        Option.when((1 << 6 & flagsByte) != 0)(Flags.ExtendedHeader),
        Option.when((1 << 5 & flagsByte) != 0)(Flags.Experimental)
      ).flatten,
      size = (sizeBytes(0) << 21) + (sizeBytes(1) << 14) + (sizeBytes(2) << 7) + sizeBytes(3)
    )
  }

  private[this] def parseExtendedHeader: ExtendedHeader = {
    file.skipBytes(10)
    val extendedHeaderBytes = new Array[Byte](10)
    file.read(extendedHeaderBytes)

    val ehSizeBytes = extendedHeaderBytes.take(4)
    val ehFlagsBytes = extendedHeaderBytes.slice(4, 6)
    val ehPaddingSizeBytes = extendedHeaderBytes.drop(6)

    val CRC32 = Option.when((1 << 7 & ehFlagsBytes.head) != 0) {
      val CRCBytes = new Array[Byte](4)
      file.read(CRCBytes)
      CRCBytes
    }

    //TODO: check sum method correctness
    ExtendedHeader(
      extendedHeaderSize = ehSizeBytes.sum,
      extendedFlags = Seq(Option.when(CRC32.isDefined)(ExtendedFlags.HasCRC)).flatten,
      sizeOfPadding = ehPaddingSizeBytes.sum,
      CRC32 = CRC32.map(_.sum)
    )
  }

  override def prettyPrint(): Unit =
    println(
      s"""
         |File: $filePath
         |
         |${header.prettySting}
         |${extendedHeader.getOrElse("")}
         |""".stripMargin
    )

  override def close(): Unit = file.close()
}
