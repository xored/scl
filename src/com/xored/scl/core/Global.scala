package com.xored.scl.core

import com.xored.scl.core.ast.Tree
import com.xored.scl.core.internal.{ Type, What }
import com.xored.scl.core.parser.Parser2
import com.xored.scl.core.command.Command

class Global() extends What with Tree with Parser2 with Command { self =>
  //TODO	
  def parser(line: String) = parse(line)
  
  def gen: Tree = null.asInstanceOf[Tree]
  
  def eval = {}
  
  private object command extends Command
  import command._
  
  def getCmd(name: String) = get(name)
  
} 