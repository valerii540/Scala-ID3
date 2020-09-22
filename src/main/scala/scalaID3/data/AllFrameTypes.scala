package scalaID3.data

import scalaID3.models.types.{CommentFrameType, FrameType, PictureFrameType, TextInfoFrameTypes, Unknown}

object AllFrameTypes {
  def matchFrameType(id: String): FrameType =
    TextInfoFrameTypes
      .get(id)
      .orElse(Option.when(id == PictureFrameType.id)(PictureFrameType))
      .orElse(Option.when(id == CommentFrameType.id)(CommentFrameType))
      .getOrElse(Unknown(id))
}
