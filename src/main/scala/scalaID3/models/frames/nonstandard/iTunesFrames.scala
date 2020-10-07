package scalaID3.models.frames.nonstandard

import scalaID3.models.FrameHeader
import scalaID3.models.frames.Frame

/** Trait for non-standard iTunes frames [[https://id3.org/iTunes]] */
sealed trait ITunesFrame extends Frame {
  val data: Array[Byte]
}

final case class ITunesTitleSortFrame(frameHeader: FrameHeader, data: Array[Byte]) extends ITunesFrame

final case class ITunesArtistSortFrame(frameHeader: FrameHeader, data: Array[Byte]) extends ITunesFrame

final case class ITunesAlbumSortFrame(frameHeader: FrameHeader, data: Array[Byte]) extends ITunesFrame

final case class ITunesAlbumArtistSortFrame(frameHeader: FrameHeader, data: Array[Byte]) extends ITunesFrame

final case class ITunesComposerSortFrame(frameHeader: FrameHeader, data: Array[Byte]) extends ITunesFrame
