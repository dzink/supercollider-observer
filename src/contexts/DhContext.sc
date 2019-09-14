DhContext : IdentityDictionary {

	/**
	 * Match any given keys in another ontext.
	 */
	match {
		arg aContext, keys = nil;
		^ this.matchAny(aContext, keys);
	}

	/**
	 * Match any given keys in another ontext.
	 */
	matchAny {
		arg aContext, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (aContext[key] == this[key]) {
				^ true;
			};
		};
		^ false;
	}

	/**
	 * Match all given keys in another context, ignoring unset.
	 */
	matchAll {
		arg aContext, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (aContext[key].isNil or: {aContext[key] != this[key]}) {
				^ false;
			};
		};
		^ true;
	}

	/**
	 * Match all given keys in another context.
	 */
	matchFull {
		arg aContext, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (aContext[key] != this[key]) {
				^ false;
			};
		};
		^ true;
	}

	/**
	 * Match no given keys in another context.
	 */
	matchNone {
		arg aContext, keys = nil;
		keys = keys ?? this.keys;
		keys.do {
			arg key;
			if (aContext[key].isNil or: {aContext[key] == this[key]}) {
				^ false;
			};
		};
		^ true;
	}

	/**
	 * Import in any unfilled keys in this based on aContext.
	 */
	merge {
		arg aContext, keys;
		if (aContext.isKindOf(DhContext)) {
		keys = keys ?? aContext.keys;
		keys.do {
			arg key;
			if (this[key].isNil) {
				this[key] = aContext[key];
				};
			};
		};
		^ this;
	}

}
