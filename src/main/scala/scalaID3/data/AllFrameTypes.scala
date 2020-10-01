package scalaID3.data

import scalaID3.models.types._

object AllFrameTypes {
  def matchFrameType(id: String): FrameType =
    // Standard frames
    TextInfoFrameTypes
      .get(id)
      .orElse(Option.when(id == PictureFrameType.id)(PictureFrameType))
      .orElse(Option.when(id == CommentFrameType.id)(CommentFrameType))
      .orElse(Option.when(id == PrivateFrameType.id)(PrivateFrameType))
      .orElse(Option.when(id == MusicCDidFrameType.id)(MusicCDidFrameType))
      .orElse(Option.when(id == PopularimeterFrameType.id)(PopularimeterFrameType))
      .orElse(Option.when(id == AudioEncryptionFrameType.id)(AudioEncryptionFrameType))
      // Non-standard frames
      .orElse(Option.when(id == NCONFrameType.id)(NCONFrameType))
      .getOrElse(Unknown(id))
}
