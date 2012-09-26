package com.xored.scl.core.command

import com.xored.scl.util.collection.Collection._


trait Command extends ReservedCommands { self =>
	protected type Parameters = _::_
	//import result._
	//TODO Nullary type argument
	// trait AbstractCommand extends (Parameters => Either[Error, Result]) with CommandApi
	
	trait AbstractCommand extends (Parameters => Either[Error, Result[_]]) with CommandApi {
		import commands._
		def apply(params: Parameters): Either[Error, Result[_]]
	}
}