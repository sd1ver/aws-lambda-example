package ru.github.sdiver
import java.util.concurrent.Executors

import cats.effect._
import ru.github.sdiver
import ru.github.sdiver.FileChangeStatus.FileUnchanged

import scala.concurrent.ExecutionContext


object HttpFileReader extends App {

  val blockingPool = Executors.newFixedThreadPool(5)
  implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutor(blockingPool)
  implicit val contextShift: ContextShift[IO] = IO.contextShift(executionContext)


  val checker: FileChecker[IO] = new HttpFileChecker
  val file = WebFile.Unknown("https://raw.githubusercontent.com/Primetalk/Funtik/master/LICENSE")
  val result = checker.resolveFileChange(file).unsafeRunSync()

  val secondRequest = result match {
    case event@FileUnchanged(fileInfo) =>
      println(event)
      val file = new sdiver.WebFile.Known(fileInfo.url, fileInfo.size - 1, fileInfo.hash)
      checker.resolveFileChange(file)
    case other => IO.raiseError(new IllegalArgumentException(s"file couldn't be $other"))
  }

  val secondResult = secondRequest.unsafeRunSync()
  println(secondResult)

  blockingPool.shutdown()
}
