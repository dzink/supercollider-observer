DhNotificationContextAll : IdentityDictionary {

	/**
	 * Match all given keys in a message.
	 */
	match {
		arg message, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (message[key] != this[key]) {
				^ false;
			};
		};
		^ true;
	}

}
