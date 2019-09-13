DhNotificationContext : IdentityDictionary {

	/**
	 * Match any given keys in a message.
	 */
	match {
		arg message, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (message[key] == this[key]) {
				^ true;
			};
		};
		^ false;
	}

	/**
	 * Match no given keys in a message.
	 */
	matchNone {
		arg message, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (message[key] == this[key]) {
				^ false;
			};
		};
		^ true;
	}



}
