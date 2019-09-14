DhContextNone : DhContext {

	/**
	 * Match no given keys in a message.
	 */
	match {
		arg aContext, keys = nil;
		^ this.matchNone(aContext, keys);
	}

}
