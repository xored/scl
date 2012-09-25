package com.xored.scl.core.ast

import com.xored.scl.core.Global

trait TreeBuilder {
	val global: Global
	import global._
	
	def makeCommand: Tree
	
	def makeBlock: Tree
	
	def makeBind: Tree
	
	def makeLambda: Tree
}