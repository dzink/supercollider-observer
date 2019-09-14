DhContextFull : DhContext {

	/**
	 * Match no given keys in a message.
	 */
	match {
		arg aContext, keys = nil;
		^ this.matchFull(aContext, keys);
	}

}
