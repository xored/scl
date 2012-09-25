package com.xored.scl.interpreter

import scala.Console

import scala.tools.nsc._
import scala.tools.nsc.io.File
import scala.tools.nsc.Global
import scala.tools.nsc.GenericRunnerCommand._
import scala.tools.nsc.interpreter.{ ILoop, IMain }
import scala.tools.nsc.Properties.{ versionString, copyrightString }

import java.io._


object PreproccessingRunner {
  
  /*
   * Skip all arguments by default
   */
  def runSclShell(args: Array[String] = Array()) {  
    /*
     * Give the parser 
     * args to parse match {
     * 	case Left(error) => TODO
     * 	case Right(Ok) => TODO
     * }
     */
    process(args)  
  }
  
  def errorFn(str: String): Boolean = {
    Console.err println str
    false
  }
  
  def process(args: Array[String]): Boolean = {
    val command = new GenericRunnerCommand(args.toList, (x: String) => errorFn(x))
    import command.{ settings, howToRun, thingToRun }
    new SclILoop process settings
  }
}

/*
 *  Entry point to the Scala command line shell
 */
object SclMain {
	
  def main(args: Array[String]) {
    PreproccessingRunner.runSclShell()
  } 
}