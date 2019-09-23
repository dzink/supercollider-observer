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

	clearAt {
		arg key;
		key = key.asString;
		if (key.contains("*")) {
			DhWildcard.wildcardMatchAll(this.keys, key).do {
				arg removeKey;
				this.removeAt(removeKey);
			};
			^ this;
		};
		this.removeAt(key);
		^ this;
	}

	clearAll {
		this.clear;
		validKeys.clear;
		^ this;
	}

}
