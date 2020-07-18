import java.io.File

import scalaID3.ID3Tag

import scala.util.{Failure, Success, Try}

object Main extends App {
  list()

  def list(): Unit = {
    val musicDir = new File("/home/proton/Music")

    musicDir.listFiles.toList.zipWithIndex.foreach { fileAndIndex =>
      Try (new ID3Tag(fileAndIndex._1.getAbsolutePath)) match {
        case Success(id3tag) =>
          println(
            s"""
               |# ${fileAndIndex._2 + 1}
               |song: ${fileAndIndex._1.getName}
               |
               |ID3 version: ${id3tag.header.version}
               |
               |${id3tag.getFrames.mkString("\n")}
               |----------------------
               |""".stripMargin)
          id3tag.close()
        case Failure(exception) =>
          println(
            s"""
               |# ${fileAndIndex._2 + 1}
               |song: ${fileAndIndex._1.getName}
               |
               |EXCEPTION: ${exception.getMessage}
               |----------------------
               |""".stripMargin)
      }




    }
  }
}
