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
		key = key.asSymbol;
		validKeys.removeAt(key);
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

	sortByProperty {
		arg property = \weight;
		var keys = this.sortKeysByProperty(property);
		^ keys.collect({
			arg key;
			this[key];
		});
	}

	size {
		^ validKeys.size();
	}
}
