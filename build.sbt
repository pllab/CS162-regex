name := "162f18"
version := "0.1"
organization := "edu.ucsb.cs.pllab"

scalacOptions ++= Seq(
  "-deprecation",
  "-feature"
)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)

initialCommands in console := """import edu.ucsb.cs.cs162._; import edu.ucsb.cs.cs162.range_set._; import edu.ucsb.cs.cs162.regex._; import edu.ucsb.cs.cs162.regex.Regex._"""

initialCommands in consoleQuick := ""

// Required so that tests that are timed out using Timeout will have their
// threads terminated when testing is finished.
fork in Test := true
