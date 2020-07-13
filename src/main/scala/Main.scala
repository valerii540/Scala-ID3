import scalaID3.ID3Tag
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes
import scalaID3.models.frames.FrameTypes

object Main extends App {
  val filePath = "test.mp3"

  val ID3Parser = new ID3Tag(filePath)

  ID3Parser.prettyPrint()

  ID3Parser.getPictureAsFile("image", PictureTypes.FrontCover)

  ID3Parser.getTextInfoFrame(FrameTypes.Album).get.value

  printID3()

  ID3Parser.close()

  def printID3(): Unit =
    println(
      s"""
        |Band:  ${ID3Parser.getTextInfoFrame(FrameTypes.Band).map(_.value)}
        |Title: ${ID3Parser.getTextInfoFrame(FrameTypes.Title).map(_.value)}
        |Album: ${ID3Parser.getTextInfoFrame(FrameTypes.Album).map(_.value)}
        |""".stripMargin
    )
}
