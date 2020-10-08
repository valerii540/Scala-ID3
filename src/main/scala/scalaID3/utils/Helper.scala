package scalaID3.utils

import java.io.RandomAccessFile
import java.nio.ByteBuffer

import scala.collection.mutable

object Helper {
  val removeExtensionRegex = "[.][^.]+$"

  //TODO: remove as unused
  implicit class IteratorExt(val array: Iterator[Byte]) {
    def toByteBuffer: ByteBuffer = ByteBuffer.wrap(array.toArray)
  }

  //FIXME: garbage spawning. Find more efficient file traversing method
  implicit class RandomAccessFileExt(val file: RandomAccessFile) {
    def take(n: Int): IndexedSeq[Byte] = {
      val tmp = new Array[Byte](n)
      file.read(tmp)
      tmp.toIndexedSeq
    }

    def take(n: Int, offset: Int): IndexedSeq[Byte] = {
      val tmp = new Array[Byte](n)
      file.read(tmp, offset, n)
      tmp.toIndexedSeq
    }

    def takeTerminatedBytes: IndexedSeq[Byte] = {
      val list = mutable.ListBuffer.empty[Byte]

      var go = true
      while (go) {
        val byte = file.readByte()
        if (byte == 0) go = false

        list += byte
      }
      list.toIndexedSeq
    }
  }
}
