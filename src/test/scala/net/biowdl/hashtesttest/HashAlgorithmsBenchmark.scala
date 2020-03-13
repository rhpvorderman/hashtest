package net.biowdl.hashtesttest

import java.io.InputStream
import java.util
import java.util.Calendar

import better.files.{File, Resource}
import net.biowdl.hashtest.HashAlgorithms
import org.scalatest.{Assertion, Matchers}
import org.scalatest.testng.TestNGSuite
import org.testng.annotations.{DataProvider, Test}

class HashAlgorithmsBenchmark {
  val randombase64File: File = File(Resource.getUrl("randombase64"))


  def benchmark(hashFunction: InputStream => String, file: File): Float = {
    val start: Long = Calendar.getInstance().getTimeInMillis
    hashFunction(file.newInputStream)
    val end: Long = Calendar.getInstance().getTimeInMillis
    val seconds: Float = (end - start ) / 1000
    seconds
  }

  def hyperfine(hashFunction: InputStream => String,
                file: File,
                testName: String,
                warmup: Int = 3,
                runs: Int = 10): Unit = {
    println(s"Perform $warmup warming up runs for $testName.")
    for (_ <- 1 to warmup) {
      hashFunction(file.newInputStream)
    }
    val results = Array[Float](runs)
    println(s"Perform $runs test runs for $testName.")
    for (i <- 0 until runs) {
      val result = benchmark(hashFunction, file)
      results(i) = result
      println(s"Run $i: $result seconds)}")
    }
    val min = results.foldLeft()
  }
}
