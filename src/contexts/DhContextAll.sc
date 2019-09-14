DhContextAll : DhContext {

	/**
	 * Match all given keys in a message.
	 */
	match {
		arg aContext, keys = nil;
		^ this.matchAll(aContext, keys);
	}

}
