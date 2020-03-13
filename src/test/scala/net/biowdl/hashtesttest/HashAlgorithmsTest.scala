package net.biowdl.hashtesttest

import better.files.{File, Resource}
import net.biowdl.hashtest.HashAlgorithms
import org.scalatest.Matchers
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{DataProvider, Test}


class HashAlgorithmsTest extends TestNGSuite with Matchers {
  val rabarberTextFile = File(Resource.getUrl("rabarber.txt"))
  val randombase64File = File(Resource.getUrl("randombase64"))
  val bigBwaIndexFile = File(Resource.getUrl("BigBwaIndex.tar.gz"))

  @DataProvider
  def md5sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "a55ba54dcb2a25f82297444e39e9d3c7"),
    Array(randombase64File, "5f8b305bde8eea31fc8fa95eff4a4cbd"),
    Array(bigBwaIndexFile, "b647006b7ea4bd7f0b54fcfe83cb1f99")
  )

  @DataProvider
  def xxh64sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "1134f95abc61dad5"),
    Array(randombase64File, "307f628745ea1f74"),
    Array(bigBwaIndexFile, "ac3113424e6e2231")
  )

  @DataProvider
  def xxh32sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "80dd1348"),
    Array(randombase64File, "0c2a8311"),
    Array(bigBwaIndexFile, "83fc7bc2")
  )

  @Test(dataProvider = "md5sums")
  def testMd5(file: File, checksum: String): Unit = {
    HashAlgorithms.md5(file.newInputStream) shouldBe checksum
  }

  @Test(dataProvider = "xxh64sums")
  def testxxh64OpenHft(file: File, checksum: String): Unit = {
    HashAlgorithms.xxh64OpenHft(file.newInputStream) shouldBe checksum
  }

  @Test(dataProvider = "xxh64sums")
  def testxxH64lz4(file: File, checksum: String): Unit = {
    HashAlgorithms.xxh64lz4(file.newInputStream) shouldBe checksum
  }

  @Test(dataProvider = "xxh32sums")
  def testxxH32lz4(file: File, checksum: String): Unit = {
    HashAlgorithms.xxh32lz4(file.newInputStream) shouldBe checksum
  }
}
