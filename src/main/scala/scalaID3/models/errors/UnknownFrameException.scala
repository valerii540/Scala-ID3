package scalaID3.models.errors

case class UnknownFrameException(message: String) extends ID3Exception(message)
