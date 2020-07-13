package scalaID3.models.frames

import scalaID3.models.FrameHeader
import scalaID3.models.frames.AttachedPictureFrame.PictureTypes.PictureType
import scalaID3.models.frames.TextInfoFrame.TextEncodings.Encoding

final case class AttachedPictureFrame(frameHeader: FrameHeader,
                                      textEncoding: Encoding,
                                      mimeType: String,
                                      pictureType: PictureType,
                                      description: String,
                                      pictureData: Array[Byte])
    extends Frame {
  override def prettySting: String =
    s"""|--- ATTACHED PICTURE FRAME (${frameHeader.frameId}) ---
        |${frameHeader.prettySting}
        |----- BODY -----
        |textEncoding: $textEncoding
        |mimeType: $mimeType
        |pictureType: ${pictureType}
        |description: $description
        |pictureData size: ${pictureData.length} B
        |""".stripMargin.replaceAll("\n\n", "\n")
}

object AttachedPictureFrame {
  object PictureTypes extends Enumeration {
    type PictureType = Value

    val Other, FileIcon, OtherFileIcon, FrontCover, BackCover, LeafletPage, Media, Lead, Artist, Conductor, Band, Composer, Lyricist,
    RecordingLocation, DuringRecording, DuringPerformance, VideoScreenshot, ColouredFish, Illustration, BandLogo, PublisherLogo = Value
  }
}
