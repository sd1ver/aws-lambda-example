package ru.github.sdiver

sealed trait WebFile {
  def url: String
}

object WebFile {

  case class Unknown(url: String) extends WebFile

  class Known(override val url: String, val size: Long, val hash: BigInt) extends WebFile {
    override def toString = s"Known($url, $size, $hash)"
  }

  case class Stored(fileId: Long, override val url: String, override val size: Long, override val hash: BigInt)
      extends Known(url, size, hash)

}
