package com.xored.scl.util.collection


/* See Haskell HList or `shapeless` library by Miles Sabin 
 * TODO HList or HMap
 */

trait Collection {
	
	sealed trait HList {
	  def isEmpty: Boolean = false
	  def length: Int
	  def head: Any = throw new NoSuchElementException
	  def tail: Any = throw new UnsupportedOperationException("tail of empty list")
	}
	
	case object HNil extends HList {
	  def ::[T](value: T) = HCons(value, this)
	  override def isEmpty = true
	  def length: Int = 0
	}
	Nil.head
	final case class HCons[H, T <: HList](override val head: H, override val tail: T) extends HList {
	  def ::[T](value: T) = HCons(value, this)
	  def length = tail.length + 1
	}
	
	type ::[H, T <: HList] = HCons[H, T]
}

object Collection extends Collection {
  
}