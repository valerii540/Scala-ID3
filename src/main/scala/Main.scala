import scalaID3.ID3Parser

object Main extends App {
  val filePath = "test.mp3"

  val ID3Parser = new ID3Parser(filePath)

  ID3Parser.print()

  ID3Parser.close()
}
