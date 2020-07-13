package scalaID3.models

import scalaID3.models.enums.ExtendedFlags.ExtendedFlag
import scalaID3.models.enums.Flags.Flag
import scalaID3.models.enums.FrameFlags.FrameFlag

final case class Header(id: String,
                        version: String,
                        flags: Set[Flag],
                        size: Int,
                        extendedHeaderSize: Option[Int],
                        extendedFlags: Option[Set[ExtendedFlag]],
                        sizeOfPadding: Option[Int],
                        CRC32: Option[Int]) {
  def prettySting: String =
    s"""|--- HEADER ---
        |id: $id
        |version: $version
        |flags: ${flags.mkString(", ")}
        |size: $size
        |${extendedHeaderSize.map("extended header size: " + _).getOrElse("")}
        |${extendedFlags.map("extended flags: " + _).getOrElse("")}
        |${sizeOfPadding.map("size of padding: " + _).getOrElse("")}
        |${CRC32.map("CRC32: " + _).getOrElse("")}
        |""".stripMargin.replaceAll("\n\n", "\n")
}

final case class FrameHeader(frameId: String,
                             size: Int,
                             flags: Set[FrameFlag],
                             decompressedSize: Option[Int],
                             encryption: Option[Byte],
                             group: Option[Byte]) {
  def prettySting: String =
    s"""|----- FRAME HEADER -----
        |id: $frameId
        |size: $size
        |flags: ${flags.mkString(", ")}
        |""".stripMargin

}
