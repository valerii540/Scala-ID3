package scalaID3.models.types

case class Unknown(something: String) extends FrameType {
  override val id: String = something
}
