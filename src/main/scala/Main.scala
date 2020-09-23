import java.io.File

import scalaID3.ID3Tag

import scala.util.{Failure, Success, Try}

object Main {
  val musicFolderPath = "/home/proton/Music"

  def main(args: Array[String]): Unit =
    if (args.length > 0 && args.head != "all") {
      println(s"song: ${args.head}")

      Try(new ID3Tag(musicFolderPath + "/" + args.head)) match {
        case Success(id3tag) =>
          println(s"""
               |ID3 version: ${id3tag.header.version}
               |
               |${id3tag.getFrames.mkString("\n")}
               |""".stripMargin)
          id3tag.close()
        case Failure(exception) =>
          throw exception
      }
    } else
      list()

  def list(): Unit = {
    val musicDir = new File(musicFolderPath)

    musicDir.listFiles.toList.zipWithIndex.foreach { fileAndIndex =>
      println(s"# ${fileAndIndex._2 + 1}\nsong: ${fileAndIndex._1.getName}")
      Try(new ID3Tag(fileAndIndex._1.getAbsolutePath)) match {
        case Success(id3tag) =>
          println(s"""
               |ID3 version: ${id3tag.header.version}
               |
               |${id3tag.getFrames.mkString("\n")}
               |----------------------
               |""".stripMargin)
          id3tag.close()
        case Failure(exception: AssertionError) =>
          println(s"""
               |EXCEPTION: ${exception.getMessage}
               |----------------------
               |""".stripMargin)
        case Failure(exception) =>
          println(s"""
               |# ${fileAndIndex._2 + 1}
               |song: ${fileAndIndex._1.getName}
               |----------------------
               |""".stripMargin)
          throw exception
      }
    }
  }
}
