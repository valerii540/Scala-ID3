package scalaID3.models

import scalaID3.models.enums.ExtendedFlags.ExtendedFlag
import scalaID3.models.enums.Flags.Flag
import scalaID3.models.enums.FrameFlags.FrameFlag
import scalaID3.models.enums.FrameTypes.FrameType

final case class ID3Header(id: String,
                           version: String,
                           flags: Set[Flag],
                           size: Int,
                           extendedHeaderSize: Option[Int],
                           extendedFlags: Option[Set[ExtendedFlag]],
                           sizeOfPadding: Option[Int],
                           CRC32: Option[Int])

final case class FrameHeader(frameType: FrameType,
                             size: Int,
                             flags: Set[FrameFlag],
                             decompressedSize: Option[Int],
                             encryption: Option[Byte],
                             group: Option[Byte])
