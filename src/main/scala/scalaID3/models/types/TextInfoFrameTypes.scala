package scalaID3.models.types

import scala.reflect.runtime
import scala.reflect.runtime.universe.ModuleSymbol

sealed trait TextInfoFrameType extends FrameType

object TextInfoFrameTypes {
  case object Album               extends TextInfoFrameType { val id = "TALB" } // ok
  case object Bpm                 extends TextInfoFrameType { val id = "TBPM" } // ok
  case object Composers           extends TextInfoFrameType { val id = "TCOM" } // '/' separated
  case object ContentType         extends TextInfoFrameType { val id = "TCON" } // +- ok
  case object Copyright           extends TextInfoFrameType { val id = "TCOP" } // +- ok
  case object Date                extends TextInfoFrameType { val id = "TDAT" } // always DDMM
  case object EncodingTime        extends TextInfoFrameType { val id = "TDEN" } // subset of ISO 8601
  case object OriginalReleaseTime extends TextInfoFrameType { val id = "TDOR" } // subset of ISO 8601
  case object RecordingTime       extends TextInfoFrameType { val id = "TDRC" } // subset of ISO 8601
  case object ReleaseTime         extends TextInfoFrameType { val id = "TDRL" } // subset of ISO 8601
  case object TaggingTime         extends TextInfoFrameType { val id = "TDTG" } // subset of ISO 8601
  case object Delay               extends TextInfoFrameType { val id = "TDLY" } // millis
  case object EncodedBy           extends TextInfoFrameType { val id = "TENC" } // ok
  case object Writers             extends TextInfoFrameType { val id = "TEXT" } // '/' separated
  case object AudioType           extends TextInfoFrameType { val id = "TFLT" } // ok
  case object InvolvedPeople      extends TextInfoFrameType { val id = "TIPL" } // ok
  case object Time                extends TextInfoFrameType { val id = "TIME" } // always HHMM
  case object ContentGroup        extends TextInfoFrameType { val id = "TIT1" } // ok
  case object Title               extends TextInfoFrameType { val id = "TIT2" } // ok
  case object Subtitle            extends TextInfoFrameType { val id = "TIT3" } // ok
  case object InitialKey          extends TextInfoFrameType { val id = "TKEY" } // always 3 chars length
  case object Languages           extends TextInfoFrameType { val id = "TLAN" } // ISO-639-2, separated by what?
  case object Length              extends TextInfoFrameType { val id = "TLEN" } // length in milliseconds
  case object MusicianCredits     extends TextInfoFrameType { val id = "TMCL" } // ok?
  case object MediaType           extends TextInfoFrameType { val id = "TMED" } // may be a reference to predefined thing
  case object Mood                extends TextInfoFrameType { val id = "TMOO" }
  case object OriginalAlbum       extends TextInfoFrameType { val id = "TOAL" } // ok
  case object OriginalFilename    extends TextInfoFrameType { val id = "TOFN" } // ok
  case object OriginalWriters     extends TextInfoFrameType { val id = "TOLY" } // '/' separated
  case object OriginalArtists     extends TextInfoFrameType { val id = "TOPE" } // '/' separated
  case object OriginalReleaseDate extends TextInfoFrameType { val id = "TORY" } // same as TYER formatting
  case object FileOwner           extends TextInfoFrameType { val id = "TOWN" } // ok
  case object Leaders             extends TextInfoFrameType { val id = "TPE1" } // '/' separated
  case object Band                extends TextInfoFrameType { val id = "TPE2" } // ok
  case object Conductor           extends TextInfoFrameType { val id = "TPE3" } // ok
  case object ModifiedBy          extends TextInfoFrameType { val id = "TPE4" } // ok
  case object PartOfSet           extends TextInfoFrameType { val id = "TPOS" } // may be extended with '/'
  case object ProducedNotice      extends TextInfoFrameType { val id = "TPRO" } // YYYY notice
  case object Publisher           extends TextInfoFrameType { val id = "TPUB" } // ok
  case object TrackNumber         extends TextInfoFrameType { val id = "TRCK" } // may be extended with '/'
  case object RecordingDates      extends TextInfoFrameType { val id = "TRDA" } // ',' separated
  case object InternetRadio       extends TextInfoFrameType { val id = "TRSN" } // ok
  case object InternetRadioOwner  extends TextInfoFrameType { val id = "TRSO" } // ok
  case object AlbumSortOrder      extends TextInfoFrameType { val id = "TSOA" } // ok
  case object PerformerSortOrder  extends TextInfoFrameType { val id = "TSOP" } // ok
  case object TitleSortOrder      extends TextInfoFrameType { val id = "TSOT" } // ok
  case object AudioSize           extends TextInfoFrameType { val id = "TSIZ" } // number
  case object ISRC                extends TextInfoFrameType { val id = "TSRC" } // 12 characters
  case object EncoderWithSettings extends TextInfoFrameType { val id = "TSSE" } // ok
  case object SetSubtitle         extends TextInfoFrameType { val id = "TSST" } // ok
  case object Year                extends TextInfoFrameType { val id = "TYER" } // always 4 characters
  case object UserDefinedText     extends TextInfoFrameType { val id = "TXXX" } // ok

  private val types = {
    val mirror = runtime.currentMirror

    mirror
      .classSymbol(TextInfoFrameTypes.getClass)
      .info
      .members
      .collect {
        case module: ModuleSymbol => mirror.reflectModule(module.asModule).instance.asInstanceOf[TextInfoFrameType]
      }
  }

  def get(id: String): Option[TextInfoFrameType] = types.find(_.id == id)
}
