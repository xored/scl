package com.xored.scl.core.command

import com.xored.scl.util.collection.Collection._

trait Command extends ReservedCommands { self =>
	protected type Parameters = HList
	
	//TODO
	// trait AbstractCommand extends (Parameters => Either[Error, Result]) with CommandApi
	trait AbstractCommand extends (Parameters => Any) with CommandApi {
		import commands._
		def apply(params: Parameters): Any
	}
}