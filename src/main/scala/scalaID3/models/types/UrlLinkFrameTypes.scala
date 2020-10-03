package scalaID3.models.types

sealed trait UrlLinkFrameType extends FrameType

object UrlLinkFrameTypes {
  case object CommercialInformation extends UrlLinkFrameType { val id = "WCOM" }
  case object UserDefinedUrlLink    extends UrlLinkFrameType { val id = "WXXX" }

  //TODO: Is reflection needed?
  private lazy val types = List(CommercialInformation, UserDefinedUrlLink)

  def get(id: String): Option[UrlLinkFrameType] = types.find(_.id == id)
}
