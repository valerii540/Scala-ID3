package scalaID3.models

sealed case class Header(id: String, version: String, flags: Seq[Flag], size: Short) {
  def prettySting: String =
    s"""|id: $id
        |version: $version
        |flags: ${flags.mkString(", ")}
        |size: $size
        |""".stripMargin
}
