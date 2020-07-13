package scalaID3.models.frames

object FrameTypes extends Enumeration {
  type FrameType = Value

  val
  ContentGroup, Title, Subtitle, Lead, Band, Conductor, ModifiedBy, Year, PartOfSet, TrackNum, Album, ContentType, Copyright, Bpm, Composer, Date,
  Time, Delay, EncodedBy, Writers, AudioType, InitialKey, Languages, Length, MediaType, OriginalAlbum, OriginalFilename, OriginalWriters,
  OriginalArtists, OriginalReleaseDate, FileOwner, Publisher, RecordingDates, InternetRadio, Picture = Value

  val frameTypesDict =
    Map(
      ContentGroup        -> "TIT1",
      Title               -> "TIT2",
      Subtitle            -> "TIT3",
      Lead                -> "TPE1",
      Band                -> "TPE2",
      Conductor           -> "TPE3",
      ModifiedBy          -> "TPE4",
      Year                -> "TYER",
      PartOfSet           -> "TPOS",
      TrackNum            -> "TRCK",
      Album               -> "TALB",
      ContentType         -> "TCON",
      Copyright           -> "TCOP",
      Bpm                 -> "TBPM",
      Composer            -> "TCOM",
      Date                -> "TDAT",
      Time                -> "TIME",
      Delay               -> "TDLY",
      EncodedBy           -> "TENC",
      Writers             -> "TEXT",
      AudioType           -> "TFLT",
      InitialKey          -> "TKEY",
      Languages           -> "TLAN",
      Length              -> "TLEN",
      MediaType           -> "TMED",
      OriginalAlbum       -> "TOAL",
      OriginalFilename    -> "TOFN",
      OriginalWriters     -> "TOLY",
      OriginalArtists     -> "TOPE",
      OriginalReleaseDate -> "TORY",
      FileOwner           -> "TOWN",
      Publisher           -> "TPUB",
      RecordingDates      -> "TRDA",
      InternetRadio       -> "TRSN",
      Picture             -> "APIC"
    )
}
