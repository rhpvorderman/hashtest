package net.biowdl.hashtest

import net.openhft.hashing.LongHashFunction
import net.jpountz.xxhash.StreamingXXHash64
import net.jpountz.xxhash.StreamingXXHash32
import org.apache.commons.codec.digest.DigestUtils
import java.io.BufferedInputStream
object HashAlgorithms {
  lazy val defaultBufferSize = 32 * 1024
  def md5(inputStream: BufferedInputStream): String = {
    DigestUtils.md5Hex(inputStream)
  }

  def xxh64OpenHft(inputStream: BufferedInputStream,
                   bufferSize: Int = defaultBufferSize
                  ): String = {
    val buffer: Array[Byte] = new Array[Byte](bufferSize)
    var xxh64sum: Long = 0L
    while (inputStream.available() > 0) {
      inputStream.read(buffer)
      xxh64sum = LongHashFunction.xx(xxh64sum).hashBytes(buffer)
    }
    xxh64sum.toHexString
  }

  def xxh64lz4(inputStream: BufferedInputStream,
               buffersize: Int = defaultBufferSize): String = {
    val hasher = StreamingXXHash64

  }
}

