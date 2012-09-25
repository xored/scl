package com.xored.scl.core.command

trait ErrorApi {
  val message: String
}

abstract class ErrorExtractor extends ErrorApi

object Error extends ErrorExtractor {
  val message = "error"
}
