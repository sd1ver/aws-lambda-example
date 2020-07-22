package ru.github.sdiver

import java.io.{InputStream, OutputStream}

import com.amazonaws.services.lambda.runtime.{Context, RequestStreamHandler}

class RequestHandler extends RequestStreamHandler{
  override def handleRequest(input: InputStream, output: OutputStream, context: Context): Unit = ???
}
