package com.xored.scl.core.parser

import com.xored.scl.core.ast.Tree
import com.xored.scl.core.internal.Type
import scala.util.parsing.combinator.JavaTokenParsers
import scala.util.parsing.combinator.Parsers

//TODO
// balanced parentheses and rec. call

trait Parser2 extends JavaTokenParsers {
  private val reserved = List("date", "time", "get-files")
  val namedParameters = List("-hello", "-world") 
  
  protected def parse(line: String): ParseResult[Any] = parseAll(exprParser, line) 
    exprParser
    
  private lazy val exprParser: Parser[Any] =
    commandParser ~ withParser |
    commandParser |
    blockParser |
    ifParser
      
  private lazy val commandParser: Parser[Any] =
    commandNameParser ~ rep1sep(paramsParser,"""(\s*)""".r) |
    commandNameParser
  
  private lazy val commandNameParser: Parser[Any] = 
    commandNameRegExp .filter(s => reserved.contains(s))
    
  private lazy val commandNameRegExp: Parser[Any] = """([a-zA-Z-0-9]*)""".r  
  
  private lazy val paramsParser: Parser[Any] = 
    paramNamedParser | 
    paramValuesParser |
    exprParser
    
  private lazy val paramNamedParser: Parser[Any] =
    paramName |
    paramName ~ paramValuesParser
    
  private lazy val paramName: Parser[Any] = """(-)([a-zA-Z]*)""".r 
  	.filter(c => namedParameters.contains(c))
  
  private lazy val paramValuesParser: Parser[Any] = 
	stringLiteral | 
    floatingPointNumber | 
    wholeNumber | 
    decimalNumber |
    exprParser
    
  private lazy val blockParser: Parser[Any] = 
    bracelParser ~> exprParser <~ bracerParser |
    ident
  
  private lazy val bracelParser: Parser[Any] = "{"
  private lazy val bracerParser: Parser[Any] = "}"
    
  private lazy val ifParser: Parser[Any] = 
    ifLiteralParser ~> ifBodyParser ~> elseParser |
    ifLiteralParser ~> ifBodyParser
    
    
  private lazy val ifLiteralParser: Parser[Any] = 
    "if" <~ "(" ~ ifCondParser <~ ")"
    
  private lazy val ifCondParser: Parser[Any] = 
    booleanParser
    bracelParser ~ blockParser ~ bracerParser
    
  private lazy val ifBodyParser: Parser[Any] = 
    blockParser | 
    bracelParser ~> blockParser <~ bracerParser |
    elseLiteralParser ~> elseBodyParser | 
    elseLiteralParser ~> elseBodyParser ~> ifParser |
    booleanParser
    
  
  private lazy val elseParser: Parser[Any] =  
    elseLiteralParser ~> bracelParser ~> elseBodyParser <~ bracerParser  
    
  private lazy val elseLiteralParser: Parser[Any] = 
    "else"
    
  private lazy val elseBodyParser: Parser[Any]= blockParser
    
//  def lambdaParser: Parser[Any] = 
//    "\\" ~> """([a-zA-Z]*)""".r ~> "->" ~> lambdaAbstractBody
//  
//  def lambdaAbstractBody: Parser[Any]
    
  private lazy val withParser: Parser[Any] =
    "with" ~> exprParser
  
  //def literalParser: Parser[Any] 

  //def digitParser: Parser[Any]
    
  private lazy val booleanParser: Parser[Any] = 
    "true" | "false" 
  
  private lazy val piperParser: Parser[Any] =
    ("|")
  
}

object TestParser2 extends Parser2 {
  import com.xored.scl.core.Global
  import com.xored.scl.util.collection.Collection._
  val programm = "if (true) \n false \n else {\ntrue\n}"
  val comm = "get-files -hello 2"
  def main(args: Array[String]) {
    //println(parseAll(commandParser, new scala.util.parsing.input.CharArrayReader(comm.toCharArray)))
    val global = new Global()
    println(global.parser("get-files -hello 10"))
    println(global.parser("date"))
    println(global.getCmd("get-files").get("D:\\"::HNil)+"\n")
  }
}