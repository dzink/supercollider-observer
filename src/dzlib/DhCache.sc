DhCache : DhNillable {

	cacheAside {
		arg key, default = {};
		if (this.includesKey(key).not) {
			var v = default.value();
			this.put(key, v);
			^ v;
		};
		^ this[key];
	}

	clear {
		arg key;
		key = key.asSymbol();
		if (this.includesKey(key)) {
			this.removeAt(key);
			^ 1;
		};
		^ 0;
	}

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
