package scalaID3.models

import scalaID3.models.ExtendedFlags.ExtendedFlag
import scalaID3.models.Flags.Flag

final case class Header(id: String, version: String, flags: Seq[Flag], size: Int) {
  def prettySting: String =
    s"""|--- HEADER ---
        |id: $id
        |version: $version
        |flags: ${flags.mkString(", ")}
        |size: $size
        |""".stripMargin
}

final case class ExtendedHeader(extendedHeaderSize: Int, extendedFlags: Seq[ExtendedFlag], sizeOfPadding: Int, CRC32: Option[Int]) {
  def prettySting: String =
    s"""|--- EXTENDED HEADER ---
        |extended header size: $extendedHeaderSize
        |extended flags: ${extendedFlags.mkString(", ")}
        |size of padding: $sizeOfPadding
        |CRC32: ${CRC32.getOrElse("")}
        |""".stripMargin
}