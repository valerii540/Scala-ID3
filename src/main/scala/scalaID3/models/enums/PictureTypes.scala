package scalaID3.models.enums

/** Used by [[scalaID3.models.frames.standard.AttachedPictureFrame]] */
object PictureTypes extends Enumeration {
  type PictureType = Value

  val Other, FileIcon, OtherFileIcon, FrontCover, BackCover, LeafletPage, Media, Lead, Artist, Conductor, Band, Composer, Lyricist, RecordingLocation,
  DuringRecording, DuringPerformance, VideoScreenshot, ColouredFish, Illustration, BandLogo, PublisherLogo = Value
}
