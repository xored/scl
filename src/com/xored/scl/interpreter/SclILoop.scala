package com.xored.scl.interpreter

import scala.tools.nsc.{ Settings }
import scala.tools.nsc.Properties
import scala.tools.nsc.interpreter.{ ILoop }
import scala.tools.nsc.interpreter._
import scala.tools.nsc.util._
import java.io.{ BufferedReader }

class SclILoop(in0: Option[BufferedReader], protected override val out: JPrintWriter) extends ILoop(in0, out) {
	
  def this(in0: BufferedReader, out: JPrintWriter) = this(Some(in0), out)
  def this() = this(None, new JPrintWriter(Console.out, true))
  
  override def printWelcome() {
    import Properties._
    val msg = s"""|Welcome to Scala Command Language %s.
    			  |Works over the Scala REPL (%s, Java %s).
    		      |$versionMsg""".stripMargin.format("Demo 0.1", javaVmName, javaVersion)
    out.println(msg)
    out.flush()
  }
  
  private def readWhile(cond: String => Boolean) = {
    Iterator continually in.readLine("") takeWhile (x => x != null && cond(x))
  }
  
  override def prompt = "scl>"
  
  private object paste extends Pasted {
    val ContinueString = "     | "
    val PromptString   = "scl> "

    def interpret(line: String): Unit = {
      echo(line.trim)
      intp interpret line
      echo("")
    }
    
    def transcript(start: String) = {
      echo("\n// Detected repl transcript paste: ctrl-D to finish.\n")
      apply(Iterator(start) ++ readWhile(_.trim != PromptString.trim))
    }
  }
  
  import LoopCommand.{ cmd, nullary }
  
  override lazy val standardCommands = List(
    cmd("about", "", "print this summary about SCL", aboutCommand),
    cmd("nice", "", "print for fun", niceDay)
  )
  
  def aboutCommand(line: String): Result = {
	  echo("Scala implementation of ECL-like DSL. Copyright (c) 2012  Xored Software, Inc.")
  }
  
  def niceDay(line: String): Result = {
	  echo("Have a nice day guys ;)")
  }
  
}

//TODO
//object SclILoop {
//  implicit def loopToInterpreter(repl: SclILoop): IMain = repl.intp
//  private def echo(msg: String) = Console println msg

//  def runForTranscript(code: String, settings: Settings): String = {
//    import java.io.{ BufferedReader, StringReader, OutputStreamWriter }
//
//    stringFromStream { ostream =>
//      Console.withOut(ostream) {
//        val output = new JPrintWriter(new OutputStreamWriter(ostream), true) {
//          override def write(str: String) = {
//            if (str forall (ch => ch.isWhitespace || ch == '|')) ()
//            // print a newline on empty scala prompts
//            else if ((str contains '\n') && (str.trim == "scl> ")) super.write("\n")
//            else super.write(str)
//          }
//        }
//        val input = new BufferedReader(new StringReader(code)) {
//          override def readLine(): String = {
//            val s = super.readLine()
//            // helping out by printing the line being interpreted.
//            if (s != null)
//              output.println(s)
//            s
//          }
//        }
//        val repl = new ILoop(input, output)
//        if (settings.classpath.isDefault)
//          settings.classpath.value = sys.props("java.class.path")
//
//        repl process settings
//      }
//    }
//  }
//
//  
//  def run(code: String, sets: Settings = new Settings): String = {
//    import java.io.{ BufferedReader, StringReader, OutputStreamWriter }
//
//    stringFromStream { ostream =>
//      Console.withOut(ostream) {
//        val input    = new BufferedReader(new StringReader(code))
//        val output   = new JPrintWriter(new OutputStreamWriter(ostream), true)
//        val repl     = new ILoop(input, output)
//
//        if (sets.classpath.isDefault)
//          sets.classpath.value = sys.props("java.class.path")
//
//        repl process sets
//      }
//    }
//  }
//  def run(lines: List[String]): String = run(lines map (_ + "\n") mkString)
//}
