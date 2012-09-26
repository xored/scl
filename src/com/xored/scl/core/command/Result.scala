package com.xored.scl.core.command

//TODO: p.2.1

trait ResultApi[+T] {
  val result: T
  def map[B >: T](f: T => B): Result[B] = Result(f(result))
  def bind[B >: T](result: B): Result[B] = Result(result)
  override def toString = "Result(" + result + ")"
}

abstract class ResultExtractor {
  def apply[T](result: T) = new Result(result)
  def unapply[T](result: Result[T]): Option[T] = Some(result.result)
}

class Result[+T](val result: T) extends ResultExtractor with ResultApi[T]

object Result extends ResultExtractor
