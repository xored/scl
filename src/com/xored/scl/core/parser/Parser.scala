package com.xored.scl.core.parser

import com.xored.scl.core._

import scala.util.parsing.combinator._
import scala.util.parsing.combinator.lexical._
import scala.util.parsing.combinator.syntactical.{ StandardTokenParsers }
import scala.util.parsing.combinator.token.{ StdTokens }
import scala.collection.mutable.{ ListBuffer, HashSet }
/*
 * 				Template Definitions
 * 
 *    
 */

//TODO
// Move to trash

class GenSourceCode private(val input: String) {

}

object T {
  
		trait Tree { 
		  
		  private[T] lazy val children = new ListBuffer[Tree] 
		  var value: Expr 
		  var left: Tree = Empty
		  var right: Tree = Empty
		  
		  def ofType: Expr#T = value.value
		  
		  def +:(child: Tree): Tree = {
		    children.+=(child)
		    println("Add child: "+child)
		    this
		  }
		  
		  def isCommand: Boolean = children isEmpty match {
		    case true => true
		    case false => children.last.ofType match {
			    case _: CommandName => true
			    case _				=> false    
			  }
		  }
			  
		  
		  def makeCommand(tree: Tree): Tree = 
		    tree.ofType match {
			    case comm@CommandName(_) => null
			    case a@Argument(int@IntDef(_)) => null
			    case a@Argument(str@StringDef(_)) => null
			    case a@Argument(comm@CommDef(_)) => null
		    }
		  
		  def getCommand: (Expr, ListBuffer[Tree]) = 
		    this.children.filter(_.isCommand) . head match {
		    	case some => (some.value, some.children) 
		    }
		    
		  
			def makeArgument(tree: Tree): Tree = 
			  tree.ofType match {
			    case comm@CommandName(_) => makeCommand(tree)
			    case _					 => {children.+=(tree); this}
			  }
		}
		
		implicit def expr2node(expr: Expr): Tree = {
		  println("implicit: "+expr)
		  new Node(expr)
		}
		
		class Node(v: Expr) extends Tree {
		  override var value: Expr = v
		  override def toString = "Node("+value+")"
		} 
		
		case object Empty extends Tree {
		  var value: Expr = _
		}
		
		

	class TreeBuild() extends Tree {
	  var value: Expr = _
	  override def toString = "Tree("+children+")"
	}
	
	trait Expr {
	  type T
	  def value: T
	  override def toString = "Expr("+value+")"
	}

	case class CommandName(v: Type) extends Expr {
	  type T = Type#T
	  def value: T = v.value
	}
	
	case class Argument(v: Type) extends Expr {
	  type T = Type#T
	  def value = v.value
	}

	implicit def string2command(name: String): CommandName = new CommandName(name)
	
	trait Type {
		type T
		val value: T
		override def toString = "Type("+value+")"
	}
	
	case class IntDef(value: Int) extends Type {
	  type T = Int
	  override def toString = "IntDef("+value+")"
	}
	
	implicit def int2Type(int: Int): Type = IntDef(int)

	case class StringDef(value: String) extends Type {
	  type T = String
	  override def toString = "StringDef("+value+")"
	}
	implicit def str2Type(str: String): Type = StringDef(str)

	case class CommDef(value: CommandName) extends Type {
	  type T = CommandName
	  override def toString = "CommDef("+value+")"
	}
	implicit def comm2Type(comm: CommandName): Type = CommDef(comm)
	
	trait Parameter[T] {
	  import scala.collection.mutable.{ ListBuffer }
	  
	  /*
	   *  TODO there need use PolyType collection
	   */
	  
	  private val args = new ListBuffer[Argument]
	  def +:(arg: Argument) = args+=arg
	}
	
}

//sealed abstract class Parameter[_] 

sealed abstract class Code
sealed abstract class Numeric
sealed abstract class StrLiteral
//sealed abstract class Command[+_[_] <: Parameter[_]]



trait SCLToken {
  val reserved = new HashSet[String]
  trait Token {
    def chars: String
  }
  case class Command(val name: String) extends Token {
    def chars = name
  }
}

object Tokenizer extends Parsers with SCLToken {
  
  def apply(reserved: Seq[String]) {
    this.reserved ++= reserved
  }
  
}


object Parser extends JavaTokenParsers {
	def apply(input: String) = parseAll(expr, input)
	
	import T._
	
	implicit val tree = new TreeBuild()
	
	val lexical = (new StdLexical).reserved+=("get-files", "date", "time", "filter")
	//TODO
	def expr(implicit t: Tree): Parser[Tree] = commandName ~> arguments .^^(arg => arg +: t) | commandName// TODO add into ast | commandName ~> ("{"|"}") ~> expr ~> ("{" | "}") | commandName
	
	def commandName(implicit t: Tree): Parser[Tree] =  comm .filter(comm => lexical.contains(comm.trim)) . ^^(comm => CommandName(comm.trim) +: t) 
	
	def arguments: Parser[Expr] = str | num
	
	def str: Parser[Argument] = stringLiteral .^^{Argument(_)}
	
	def num: Parser[Argument] = wholeNumber .^^(str => Argument(str.toInt))
	
	def num2par(parser: Parser[Int]): Parser[Argument] = parser ^^ {Argument(_)}
	
	def comm: Parser[String] = """([a-zA-Z-0-9]*)""".r 
	
	def strArg: Parser[Argument] = str2p("""\"+([a-zA-Z-0-9]*)+\"""".r)
	
	def str2p(parser: Parser[String]): Parser[Argument] = parser ^^{Argument(_)}
	
	//TODO need AST
	
	def main(args: Array[String]) { 
	  //import AbstractCommand._
	  println(apply("date"))
	  println("AST: "+tree)
	  println("Hello")
	 // fromAstToFun
	}
}