/**
 * A kind of IdentityDictionary where items can be evaluated once, and then
 * the result of that evaluation is stored.
 *
 * Mostly, you'll want to use .cacheAside(\key, { default value }). The default
 * value function can be as complex as needed, and is only evaluated if the
 * key doesn't exist yet.
 */
DhCache : DhNillable {

	/**
	 * Retrieve an item from the cache. If it does not exist yet, evaluate the
	 * function to get the correct value.
	 */
	cacheAside {
		arg key, default = {};
		if (this.includesKey(key).not) {
			var v = default.value();
			this.put(key, v);
			^ v;
		};
		^ this[key];
	}

	/**
	 * Clear the cache at a key.
	 */
	clear {
		arg key;
		key = key.asSymbol();
		if (this.includesKey(key)) {
			this.removeAt(key);
			^ 1;
		};
		^ 0;
	}

	/**
	 * Clear the cache, using a wildcard. Use this to clear all caches of a given
	 * prefix.
	 */
	clearWildcard {
		arg key;
		var matches = DhWildcard.wildcardMatchAll(this.keys, key);
		matches.do {
			arg removeKey;
			this.removeAt(removeKey);
		};
		^ matches.size;
	}

	clearAll {
		this.clear;
		validKeys.clear;
		^ this;
	}

}
