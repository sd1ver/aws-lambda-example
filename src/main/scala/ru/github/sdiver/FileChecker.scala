package ru.github.sdiver

trait FileChecker[F[_]] {

  def resolveFileChange(oldStatus: WebFile): F[FileChangeStatus]
}

