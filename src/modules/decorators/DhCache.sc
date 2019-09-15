DhCache : DhAtom {
	var <validKeys;

	*new {
		var c = super.new();
		^ c.init;
	}

	init {
		validKeys = IdentityDictionary[];
		^ this;
	}

	cacheAside {
		arg key, default;
		if (this.includesKey(key).not) {
			var v = default.value();
			this.put(key, v);
			^ v;
		};
		^ this[key];
	}

	put {
		arg key, value;
		var p = super.put(key, value);
		validKeys[key] = true;
		^ p;
	}

	removeAt {
		arg key;
		validKeys[key] = false;
		^ super.removeAt(key);
	}

	remove {
		arg value;
		var key = this.indexOf(value);
		if (key.isNil.not) {
			^ this.removeAt(key);
		};
		^ nil;
	}

	includesKey {
		arg key;
		^ validKeys[key] ?? false;
	}


}
