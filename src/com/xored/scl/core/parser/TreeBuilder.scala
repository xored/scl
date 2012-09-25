package com.xored.scl.core.parser

import com.xored.scl.core.Global

trait TreeBuilder {
  
  val global: Global
  
  import global._
  
  def makeCommand: Tree
 
  def makeParameters: Tree
  
  def makeBlock: Tree
  
  def makeLambda: Tree
  
  def makeVaribale: Tree
  
  def makeDoc: Tree
 
}
