name := "hash-test"

version := "0.1"

scalaVersion := "2.12.6"

libraryDependencies += "net.openhft" % "zero-allocation-hashing" % "0.11"
libraryDependencies += "org.lz4" % "lz4-java" % "1.7.1"
libraryDependencies += "commons-codec" % "commons-codec" % "1.11"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test
libraryDependencies += "org.testng" % "testng" % "6.14.3" % Test
libraryDependencies += "com.github.pathikrit" %% "better-files" % "3.8.0" % Test
