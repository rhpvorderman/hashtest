package net.biowdl.hashtest

import net.openhft.hashing.LongHashFunction
import net.jpountz.xxhash.XXHashFactory
import org.apache.commons.codec.digest.DigestUtils
import java.io.InputStream

object HashAlgorithms {
  lazy val defaultBufferSize = 32 * 1024
  lazy val xxhashFactory = XXHashFactory.fastestInstance()

  def md5(inputStream: InputStream): String = {
    DigestUtils.md5Hex(inputStream)
  }

  def xxh64OpenHft(inputStream: InputStream,
                   bufferSize: Int = defaultBufferSize
                  ): String = {
    val buffer: Array[Byte] = new Array[Byte](bufferSize)
    var xxh64sum: Long = 0L
    while (inputStream.available() > 0) {
      var length = inputStream.read(buffer)
      xxh64sum = LongHashFunction.xx(xxh64sum).hashBytes(buffer, 0 ,length)
    }
    // toHexString does not add leading zero's
    f"%%16s".format(xxh64sum.toHexString).replace(" ", "0")
  }

  def xxh64lz4(inputStream: InputStream,
               bufferSize: Int = defaultBufferSize): String = {
    val hasher = xxhashFactory.newStreamingHash64(0)
    val buffer: Array[Byte] = new Array[Byte](bufferSize)
    while (inputStream.available() > 0) {
      var length: Int = inputStream.read(buffer)
      hasher.update(buffer, 0, length)
    }
    // toHexString does not add leading zero's
    f"%%16s".format(hasher.getValue.toHexString).replace(" ", "0")
  }

  def xxh32lz4(inputStream: InputStream,
               bufferSize: Int = defaultBufferSize): String = {
    val hasher = xxhashFactory.newStreamingHash32(0)
    val buffer: Array[Byte] = new Array[Byte](bufferSize)
    while (inputStream.available() > 0) {
      var length: Int = inputStream.read(buffer)
      hasher.update(buffer, 0, length)
    }
    // toHexString does not add leading zero's
    f"%%8s".format(hasher.getValue.toHexString).replace(" ", "0")
  }
}

