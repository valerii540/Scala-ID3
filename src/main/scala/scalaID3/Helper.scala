package scalaID3

import java.io.RandomAccessFile
import java.nio.ByteBuffer

import scala.collection.mutable

object Helper {
  val removeExtensionRegex = "[.][^.]+$"

  implicit class IteratorExt(val array: Iterator[Byte]) {
    def toByteBuffer: ByteBuffer = ByteBuffer.wrap(array.toArray)
  }

  //FIXME: garbage spawning. Find more efficient file traversing method
  implicit class RandomAccessFileExt(val file: RandomAccessFile) {
    def take(n: Int): IndexedSeq[Byte] = {
      val tmp = new Array[Byte](n)
      file.read(tmp)
      tmp
    }

    def take(n: Int, offset: Int): IndexedSeq[Byte] = {
      val tmp = new Array[Byte](n)
      file.read(tmp, offset, n)
      tmp
    }

    def takeWhile(p: Byte => Boolean): IndexedSeq[Byte] = {
      val list = mutable.ListBuffer.empty[Byte]

      var go = true
      while (go) {
        val byte = file.readByte()
        if (p(byte)) list += byte
        else go = false
      }
      list.toIndexedSeq
    }
  }
}
