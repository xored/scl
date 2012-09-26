package com.xored.scl.core.command


trait ErrorApi {
  val message: String
}

abstract class ErrorExtractor {
  def apply(message: String) = new Error(message)
  def unapply(error: Error) = Some(error.message)
}

class Error(val message: String) extends ErrorExtractor with ErrorApi

object Error extends ErrorExtractor

