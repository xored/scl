package com.xored.scl.core.command

import com.xored.scl.core.command
import com.xored.scl.util.collection.Collection._

import scala.collection.mutable.HashMap
import java.io._
import java.util.Date


trait ReservedCommands { self: Command =>
  object commands {
    protected type CommandName = String
	private val reserved: HashMap[CommandName, AbstractCommand] = HashMap()
	
	def add(name: String, comm: AbstractCommand) {
	  reserved+=(name->comm)
	}
	
    
    //TODO 
    // should be something like is `def apply(path: String::IList)` where :: - is the type
	val getFiles = new AbstractCommand {
	  val namedParameters = List("-path")
	  def apply(path: Parameters): List[File] = 
	    new File(path.head.asInstanceOf[String]) .listFiles .toList
	}
	add("get-files", getFiles)
	
	val date = new AbstractCommand {
	  val namedParameters = List()
	  def apply(path: Parameters) = 
	    println(new Date() .toString)
	}
	add("date", date)
	
	def get(name: String) = reserved.get(name)
  }
  
  protected def get(name: String) = commands.get(name)
}