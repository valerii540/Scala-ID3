package scalaID3.models

sealed trait Flag

case object Unsynchronisation extends Flag
case object ExtendedHeader extends Flag
case object Experimental extends Flag
