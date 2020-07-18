import java.io.File

import scalaID3.ID3Tag

object Main extends App {
  list()

  def list(): Unit = {
    val musicDir = new File("/home/vbosiak/Music")

    musicDir.listFiles.toList.foreach { file =>
      val id3tag = new ID3Tag(file.getAbsolutePath)
      println(
        s"""
          |song: ${file.getName}
          |
          |${id3tag.getFrames.mkString("\n")}
          |----------------------
          |""".stripMargin)


      id3tag.close()
    }
  }
}
