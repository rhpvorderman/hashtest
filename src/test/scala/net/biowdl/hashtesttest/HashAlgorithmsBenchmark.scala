package net.biowdl.hashtesttest

import java.io.InputStream
import java.util.Calendar

import better.files.{File, Resource}
import net.biowdl.hashtest.HashAlgorithms
import org.testng.annotations.{DataProvider, Test}

import scala.collection.mutable.ListBuffer

class HashAlgorithmsBenchmark {
  val bigBwaIndexFile: File = File(Resource.getUrl("BigBwaIndex.tar.gz"))
  val md5: InputStream => String = HashAlgorithms.md5
  val xxh64: InputStream => String = HashAlgorithms.xxh64lz4(_)
  val xxh32: InputStream => String = HashAlgorithms.xxh32lz4(_)

  @DataProvider
  def benchmarks(): Array[Array[Any]] = Array(
    Array(xxh64, bigBwaIndexFile, "xxh64sum", 3, 10),
    Array(xxh32, bigBwaIndexFile, "xxh32sum", 3, 10),
    Array(md5, bigBwaIndexFile, "md5sum", 3 ,10),
  )

  def benchmark(hashFunction: InputStream => String, file: File): Long = {
    val start: Long = Calendar.getInstance().getTimeInMillis
    hashFunction(file.newInputStream)
    val end: Long = Calendar.getInstance().getTimeInMillis
    end - start
  }

  @Test(dataProvider = "benchmarks")
  def hyperfine(hashFunction: InputStream => String,
                file: File,
                testName: String,
                warmup: Int = 3,
                runs: Int = 10): Unit = {
    println(s"Perform $warmup warming up runs for $testName.")
    for (_ <- 1 to warmup) {
      hashFunction(file.newInputStream)
    }
    val results: ListBuffer[Float] = ListBuffer()
    println(s"Perform $runs test runs for $testName.")
    for (i <- 0 until runs) {
      val result = benchmark(hashFunction, file)
      results.append(result)
    }
    print(s"${testName} results. Average=${results.sum / runs}ms, min=${results.min}ms, max=${results.max}ms")
  }
}
