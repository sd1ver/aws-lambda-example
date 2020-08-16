package ru.github.sdiver

import java.security.MessageDigest

import cats.effect.{ContextShift, IO}
import fs2.Chunk
import org.http4s.{Method, Request, Response}
import org.http4s.Uri
import org.http4s.client.blaze.BlazeClientBuilder
import ru.github.sdiver.FileChangeStatus._

import scala.concurrent.ExecutionContext

class HttpFileChecker(implicit ec: ExecutionContext, contextShift: ContextShift[IO]) extends FileChecker[IO] {

  override def resolveFileChange(webFile: WebFile): IO[FileChangeStatus] = {
    webFile match {
      case webFile: WebFile.Unknown =>
        readWholeFile(webFile).map(FileUnchanged)
      case known: WebFile.Known =>
        detectChanges(known)
    }
  }

  def detectChanges(oldFile: WebFile.Known): IO[FileChangeStatus] = {
    readWholeFile(oldFile).map{ checkChange(oldFile, _)}
  }

  def checkChange(oldFile: WebFile.Known, newFile: WebFile.Known): FileChangeStatus = {
    if(oldFile != newFile){
      FileChangeStatus.FileChanged(newFile, oldFile)
    } else {
      FileUnchanged(oldFile)
    }
  }

  def readWholeFile(webFile: WebFile): IO[WebFile.Known] = {
    val request  = getRequest(webFile)
    val resource = BlazeClientBuilder[IO](ec).resource
    resource.flatMap(_.run(request)).use(knownFile(webFile))
  }

  def getRequest(webFile: WebFile): Request[IO] = {
    val uri = Uri.fromString(webFile.url).right.get
    Request[IO](Method.GET, uri)
  }

  def knownFile(webFile: WebFile)(response: Response[IO]): IO[WebFile.Known] = {
    response.body.compile
      .foldChunks(CalcResult())(calcFileMeta)
      .map(knownWebFile(webFile))
  }

  def knownWebFile(webFile: WebFile)(result: CalcResult): WebFile.Known = {
    val hashBytes = result.digest.digest()
    val hash      = BigInt(hashBytes)
    new WebFile.Known(webFile.url, result.size, hash)
  }

  private def calcFileMeta: (CalcResult, Chunk[Byte]) => CalcResult = (result, chunk) => {
    val bytes = chunk.toArray
    result.digest.update(bytes)
    result.copy(result.digest, result.size + bytes.length)
  }

  private case class CalcResult(digest: MessageDigest = MessageDigest.getInstance("MD5"), size: Int = 0)
}
