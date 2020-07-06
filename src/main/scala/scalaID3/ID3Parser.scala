package scalaID3

import java.io.RandomAccessFile

import scalaID3.models.{Experimental, ExtendedHeader, Header, Unsynchronisation}

import scala.util.{Failure, Try}

sealed trait ParserOps {
  def close(): Unit

  def print(): Unit
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

  private[this] def parseHeader: Header = {
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
        Option.when((1 << 7 & flagsByte) != 0)(Unsynchronisation),
        Option.when((1 << 6 & flagsByte) != 0)(ExtendedHeader),
        Option.when((1 << 5 & flagsByte) != 0)(Experimental)
      ).flatten,
      size = ((sizeBytes(0) << 21) + (sizeBytes(1) << 14) + (sizeBytes(2) << 7) + sizeBytes(3)).toShort
    )
  }

  override def print(): Unit =
    println(
      s"""
         |File: $filePath
         |
         |--- HEADER ---
         |${header.prettySting}
         |""".stripMargin
    )

  override def close(): Unit = file.close()
}
