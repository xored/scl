package com.xored.scl.core.ast

import com.xored.scl.core.internal
import com.xored.scl.core.internal.Universe

trait Tree extends TreeApi {
  
  
  case class CommandDef(name: String, param: List[Tree]) extends Tree
  
  case class BlockDef(body: List[Tree]) extends Tree
  
  case class LambdaDef(param: Tree, body: List[Tree]) extends Tree
  
  case class IfDef(cond: Tree, thenp: Tree, elsep: Tree) extends Tree
  
  case class TryDef(body: Tree, catchp: Tree) extends Tree
  
  case class ThrowDef(body: Tree) extends Tree
  
  case class VarDef(name: String, body: Tree) extends Tree
  
  case class WithDef(pred: Tree, succ: Tree) extends Tree 
  
  case class Bind(name: String, body: Tree) extends Tree
}