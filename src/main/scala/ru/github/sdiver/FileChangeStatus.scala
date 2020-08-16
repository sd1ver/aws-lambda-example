package ru.github.sdiver

import ru.github.sdiver.WebFile.Known

sealed trait FileChangeStatus extends Serializable

object FileChangeStatus {

  case class FileUnchanged(fileInfo: Known) extends FileChangeStatus

  case class FileChanged(newInfo: Known, oldInfo: Known) extends FileChangeStatus
}
