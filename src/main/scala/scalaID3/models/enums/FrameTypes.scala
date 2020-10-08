package scalaID3.models.enums

object FrameTypes extends Enumeration {
  type FrameType = Value

  implicit class FrameTypeExt(val frameType: FrameType) {
    def isTextInfoFrameType: Boolean = frameType.id >= 0 && frameType.id <= 51

    def isUrlLinkFrameType: Boolean = frameType.id >= 60 && frameType.id <= 68

    def isITunesFrameType: Boolean = frameType.id >= 101
  }

  // Text info frames [0:51]
  val Album: FrameType               = Value(0, "TALB")  // ok
  val Bpm: FrameType                 = Value(1, "TBPM")  // ok
  val Composers: FrameType           = Value(2, "TCOM")  // '/' separated
  val ContentType: FrameType         = Value(3, "TCON")  // +- ok
  val Copyright: FrameType           = Value(4, "TCOP")  // +- ok
  val Date: FrameType                = Value(5, "TDAT")  // always DDMM
  val EncodingTime: FrameType        = Value(6, "TDEN")  // subset of ISO 8601
  val OriginalReleaseTime: FrameType = Value(7, "TDOR")  // subset of ISO 8601
  val RecordingTime: FrameType       = Value(8, "TDRC")  // subset of ISO 8601
  val ReleaseTime: FrameType         = Value(9, "TDRL")  // subset of ISO 8601
  val TaggingTime: FrameType         = Value(10, "TDTG") // subset of ISO 8601
  val Delay: FrameType               = Value(11, "TDLY") // millis
  val EncodedBy: FrameType           = Value(12, "TENC") // ok
  val Writers: FrameType             = Value(13, "TEXT") // '/' separated
  val AudioType: FrameType           = Value(14, "TFLT") // ok
  val InvolvedPeople: FrameType      = Value(15, "TIPL") // ok
  val Time: FrameType                = Value(16, "TIME") // always HHMM
  val ContentGroup: FrameType        = Value(17, "TIT1") // ok
  val Title: FrameType               = Value(18, "TIT2") // ok
  val Subtitle: FrameType            = Value(19, "TIT3") // ok
  val InitialKey: FrameType          = Value(20, "TKEY") // always 3 chars length
  val Languages: FrameType           = Value(21, "TLAN") // ISO-639-2, separated by what?
  val Length: FrameType              = Value(22, "TLEN") // length in milliseconds
  val MusicianCredits: FrameType     = Value(23, "TMCL") // ok?
  val MediaType: FrameType           = Value(24, "TMED") // may be a reference to predefined thing
  val Mood: FrameType                = Value(25, "TMOO") // ok
  val OriginalAlbum: FrameType       = Value(26, "TOAL") // ok
  val OriginalFilename: FrameType    = Value(27, "TOFN") // ok
  val OriginalWriters: FrameType     = Value(28, "TOLY") // '/' separated
  val OriginalArtists: FrameType     = Value(29, "TOPE") // '/' separated
  val OriginalReleaseDate: FrameType = Value(30, "TORY") // same as TYER formatting
  val FileOwner: FrameType           = Value(31, "TOWN") // ok
  val Leaders: FrameType             = Value(32, "TPE1") // '/' separated
  val Band: FrameType                = Value(33, "TPE2") // ok
  val Conductor: FrameType           = Value(34, "TPE3") // ok
  val ModifiedBy: FrameType          = Value(35, "TPE4") // ok
  val PartOfSet: FrameType           = Value(36, "TPOS") // may be extended with '/'
  val ProducedNotice: FrameType      = Value(37, "TPRO") // YYYY notice
  val Publisher: FrameType           = Value(38, "TPUB") // ok
  val TrackNumber: FrameType         = Value(39, "TRCK") // may be extended with '/'
  val RecordingDates: FrameType      = Value(40, "TRDA") // ',' separated
  val InternetRadio: FrameType       = Value(41, "TRSN") // ok
  val InternetRadioOwner: FrameType  = Value(42, "TRSO") // ok
  val AlbumSortOrder: FrameType      = Value(43, "TSOA") // ok
  val PerformerSortOrder: FrameType  = Value(44, "TSOP") // ok
  val TitleSortOrder: FrameType      = Value(45, "TSOT") // ok
  val AudioSize: FrameType           = Value(46, "TSIZ") // number
  val ISRC: FrameType                = Value(47, "TSRC") // 12 characters
  val EncoderWithSettings: FrameType = Value(48, "TSSE") // ok
  val SetSubtitle: FrameType         = Value(49, "TSST") // ok
  val Year: FrameType                = Value(50, "TYER") // always 4 characters
  val UserDefinedText: FrameType     = Value(51, "TXXX") // ok

  // Url link frames [60:68]
  val CommercialInfoLink: FrameType           = Value(60, "WCOM")
  val CopyrightInfoLink: FrameType            = Value(61, "WCOP")
  val OfficialAudioFileWebpage: FrameType     = Value(62, "WOAF")
  val OfficialArtistWebpage: FrameType        = Value(63, "WOAR")
  val OfficialAudioSourceWebpage: FrameType   = Value(64, "WOAS")
  val OfficialInternetRadioWebpage: FrameType = Value(65, "WORS")
  val PaymentLink: FrameType                  = Value(66, "WPAY")
  val PublishersOfficialWebpage: FrameType    = Value(67, "WPUB")
  val UserDefinedLink: FrameType              = Value(68, "WXXX")

  // Other frames [70:x]
  val AudioEncryption: FrameType    = Value(70, "AENC")
  val Comment: FrameType            = Value(71, "COMM")
  val Commercial: FrameType         = Value(72, "COMR")
  val MusicCDid: FrameType          = Value(73, "MCDI")
  val Picture: FrameType            = Value(74, "APIC")
  val Popularimeter: FrameType      = Value(75, "POPM")
  val Private: FrameType            = Value(76, "PRIV")
  val UnsyncLyrics: FrameType       = Value(77, "USLT")
  val SyncLyrics: FrameType         = Value(78, "SYLT")
  val UniqueFileId: FrameType       = Value(79, "UFID")
  val EncapsulatedObject: FrameType = Value(80, "GEOB")

  // Non-standard frames [100:x]
  val MusicMatchNCON: FrameType          = Value(100, "NCON")
  val ITunesTitleSort: FrameType         = Value(101, "TSOT")
  val ITunesArtistSort: FrameType        = Value(102, "TSOP")
  val ITunesAlbumSort: FrameType         = Value(103, "TSOA")
  val ITunesAlbumArtistSort: FrameType   = Value(104, "TSO2")
  val ITunesComposerSort: FrameType      = Value(105, "TSOC")
  val ITunesPartOfCompilation: FrameType = Value(106, "TCMP")

  //TODO: remove as debugging symbol
  val Unknown: FrameType = Value(1000, "????")
}
