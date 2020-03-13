package net.biowdl.hashtesttest

import better.files.{File, Resource}
import net.biowdl.hashtest.HashAlgorithms
import org.scalatest.{Assertion, Matchers}
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{DataProvider, Test}
class HashAlgorithmsTest extends TestNGSuite with Matchers {
  val rabarberTextFile = File(Resource.getUrl("rabarber.txt"))
  val randombase64File = File(Resource.getUrl("randombase64"))

  @DataProvider
  def md5sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "a55ba54dcb2a25f82297444e39e9d3c7"),
    Array(randombase64File, "5f8b305bde8eea31fc8fa95eff4a4cbd")
  )

  @DataProvider
  def xxh64sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "1134f95abc61dad5"),
    Array(randombase64File, "307f628745ea1f74")
  )

  @DataProvider
  def xxh32sums(): Array[Array[Any]] = Array(
    Array(rabarberTextFile, "80dd1348"),
    Array(randombase64File, "0c2a8311")
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
