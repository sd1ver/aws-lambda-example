import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.1.1"

  object Aws {
    val lambdaEvents = "com.amazonaws" % "aws-lambda-java-events" % "3.1.0"
    val lambdaCore   = "com.amazonaws" % "aws-lambda-java-core"   % "1.2.1"
  }

}
