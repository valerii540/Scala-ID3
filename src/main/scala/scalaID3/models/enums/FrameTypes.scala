package scalaID3.models.enums

object FrameTypes extends Enumeration {
  type FrameType = Value

  val Album: FrameType               = Value("TALB") // ok
  val Bpm: FrameType                 = Value("TBPM") // ok
  val Composers: FrameType           = Value("TCOM") // '/' separated
  val ContentType: FrameType         = Value("TCON") // +- ok
  val Copyright: FrameType           = Value("TCOP") // +- ok
  val Date: FrameType                = Value("TDAT") // always DDMM
  val Delay: FrameType               = Value("TDLY") // millis
  val EncodedBy: FrameType           = Value("TENC") // ok
  val Writers: FrameType             = Value("TEXT") // '/' separated
  val AudioType: FrameType           = Value("TFLT") // ok
  val Time: FrameType                = Value("TIME") // always HHMM
  val ContentGroup: FrameType        = Value("TIT1") // ok
  val Title: FrameType               = Value("TIT2") // ok
  val Subtitle: FrameType            = Value("TIT3") // ok
  val InitialKey: FrameType          = Value("TKEY") // always 3 chars length
  val Languages: FrameType           = Value("TLAN") // ISO-639-2, separated by what?
  val Length: FrameType              = Value("TLEN") // length in milliseconds
  val MediaType: FrameType           = Value("TMED") // may be a reference to predefined thing
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
  val Publisher: FrameType           = Value("TPUB") // ok
  val TrackNumber: FrameType         = Value("TRCK") // may be extended with '/'
  val RecordingDates: FrameType      = Value("TRDA") // ',' separated
  val InternetRadio: FrameType       = Value("TRSN") // ok
  val InternetRadioOwner: FrameType  = Value("TRSO") // ok
  val AudioSize: FrameType           = Value("TSIZ") // number
  val ISRC: FrameType                = Value("TSRC") // 12 characters
  val EncoderWithSettings: FrameType = Value("TSSE") // ok
  val Year: FrameType                = Value("TYER") // always 4 characters
  val UserDefinedText: FrameType     = Value("TXXX") // ok

  val Picture: FrameType = Value("APIC")

  val Unknown: FrameType = Value("unknown")
}
