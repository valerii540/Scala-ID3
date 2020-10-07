package scalaID3.models.enums

object FrameTypes extends Enumeration {
  type FrameType = Value

  // Text info frames
  val Album: FrameType               = Value("TALB") // ok
  val Bpm: FrameType                 = Value("TBPM") // ok
  val Composers: FrameType           = Value("TCOM") // '/' separated
  val ContentType: FrameType         = Value("TCON") // +- ok
  val Copyright: FrameType           = Value("TCOP") // +- ok
  val Date: FrameType                = Value("TDAT") // always DDMM
  val EncodingTime: FrameType        = Value("TDEN") // subset of ISO 8601
  val OriginalReleaseTime: FrameType = Value("TDOR") // subset of ISO 8601
  val RecordingTime: FrameType       = Value("TDRC") // subset of ISO 8601
  val ReleaseTime: FrameType         = Value("TDRL") // subset of ISO 8601
  val TaggingTime: FrameType         = Value("TDTG") // subset of ISO 8601
  val Delay: FrameType               = Value("TDLY") // millis
  val EncodedBy: FrameType           = Value("TENC") // ok
  val Writers: FrameType             = Value("TEXT") // '/' separated
  val AudioType: FrameType           = Value("TFLT") // ok
  val InvolvedPeople: FrameType      = Value("TIPL") // ok
  val Time: FrameType                = Value("TIME") // always HHMM
  val ContentGroup: FrameType        = Value("TIT1") // ok
  val Title: FrameType               = Value("TIT2") // ok
  val Subtitle: FrameType            = Value("TIT3") // ok
  val InitialKey: FrameType          = Value("TKEY") // always 3 chars length
  val Languages: FrameType           = Value("TLAN") // ISO-639-2, separated by what?
  val Length: FrameType              = Value("TLEN") // length in milliseconds
  val MusicianCredits: FrameType     = Value("TMCL") // ok?
  val MediaType: FrameType           = Value("TMED") // may be a reference to predefined thing
  val Mood: FrameType                = Value("TMOO") // ok
  val OriginalAlbum: FrameType       = Value("TOAL") // ok
  val OriginalFilename: FrameType    = Value("TOFN") // ok
  val OriginalWriters: FrameType     = Value("TOLY") // '/' separated
  val OriginalArtists: FrameType     = Value("TOPE") // '/' separated
  val OriginalReleaseDate: FrameType = Value("TORY") // same as TYER formatting
  val FileOwner: FrameType           = Value("TOWN") // ok
  val Leaders: FrameType             = Value("TPE1") // '/' separated
  val Band: FrameType                = Value("TPE2") // ok
  val Conductor: FrameType           = Value("TPE3") // ok
  val ModifiedBy: FrameType          = Value("TPE4") // ok
  val PartOfSet: FrameType           = Value("TPOS") // may be extended with '/'
  val ProducedNotice: FrameType      = Value("TPRO") // YYYY notice
  val Publisher: FrameType           = Value("TPUB") // ok
  val TrackNumber: FrameType         = Value("TRCK") // may be extended with '/'
  val RecordingDates: FrameType      = Value("TRDA") // ',' separated
  val InternetRadio: FrameType       = Value("TRSN") // ok
  val InternetRadioOwner: FrameType  = Value("TRSO") // ok
  val AlbumSortOrder: FrameType      = Value("TSOA") // ok
  val PerformerSortOrder: FrameType  = Value("TSOP") // ok
  val TitleSortOrder: FrameType      = Value("TSOT") // ok
  val AudioSize: FrameType           = Value("TSIZ") // number
  val ISRC: FrameType                = Value("TSRC") // 12 characters
  val EncoderWithSettings: FrameType = Value("TSSE") // ok
  val SetSubtitle: FrameType         = Value("TSST") // ok
  val Year: FrameType                = Value("TYER") // always 4 characters
  val UserDefinedText: FrameType     = Value("TXXX") // ok

  // Url link frames
  val CommercialInfoLink: FrameType           = Value("WCOM")
  val CopyrightInfoLink: FrameType            = Value("WCOP")
  val OfficialAudioFileWebpage: FrameType     = Value("WOAF")
  val OfficialArtistWebpage: FrameType        = Value("WOAR")
  val OfficialAudioSourceWebpage: FrameType   = Value("WOAS")
  val OfficialInternetRadioWebpage: FrameType = Value("WORS")
  val PaymentLink: FrameType                  = Value("WPAY")
  val PublishersOfficialWebpage: FrameType    = Value("WPUB")
  val UserDefinedLink: FrameType              = Value("WXXX")

  // Others
  val AudioEncryption: FrameType = Value("AENC")
  val Comment: FrameType         = Value("COMM")
  val Commercial: FrameType      = Value("COMR")
  val MusicCDid: FrameType       = Value("MCDI")
  val Picture: FrameType         = Value("APIC")
  val Popularimeter: FrameType   = Value("POPM")
  val Private: FrameType         = Value("PRIV")
  val UnsyncLyrics: FrameType    = Value("USLT")
  val UniqueFileId: FrameType    = Value("UFID")

  // Non-standard
  val NCON: FrameType = Value("NCON")

  val Unknown: FrameType = Value("????")
}
