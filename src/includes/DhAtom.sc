DhAtom : IdentityDictionary {

	at {
		arg key;
		^ super.at(key.asSymbol);
	}

	put {
		arg key, value;
		super.put(key.asSymbol, value);
		^ this;
	}

	sortByProperty {
		arg property = \weight;
		var keys = this.keys;
		var values = this.values.sortMap {
			arg value;
			this.determineWeight(value, property);
		};
		^ values;
	}

	sortKeysByProperty {
		arg property = \weight;
		var keys = this.keys;
		^ keys.asArray.sortMap {
			arg key;
			var weight = this.determineWeight(this[key], property);
			weight;
		};
	}

	determineWeight {
		arg object, property = \weight;
		if (object.isKindOf(Dictionary)) {
			^ (object[property] ?? 0);
		} {
			if (object.respondsTo(property.asSymbol)) {
				^ (object.process(property.asSymbol) ?? 0);
			} {
			^	0;
			};
		};
	}

	fullKeysValuesDo {
		arg func;
		this.keys.do {
			arg key;
			var value = this[key];
			func.value(key, value);
		};
	}

	weightedKeysValuesDo {
		arg func, property = \weight;
		this.sortKeysByProperty(property).do {
			arg key;
			var value = this[key];
			func.value(key, value);
		};
	}

}
