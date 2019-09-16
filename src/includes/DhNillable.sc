DhNillable : DhAtom {
	var <validKeys;

	*new {
		var c = super.new();
		^ c.init;
	}

	init {
		validKeys = IdentityDictionary[];
		^ this;
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

	keys {
		^ validKeys.keys();
	}

	includesKey {
		arg key;
		^ validKeys[key] ?? false;
	}
}
