DhNotificationContextNone : IdentityDictionary {

	/**
	 * Match no given keys in a message.
	 */
	match {
		arg message, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (message[key].isNil or: {message[key] == this[key]}) {
				^ false;
			};
		};
		^ true;
	}

}
