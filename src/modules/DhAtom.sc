DhAtom : IdentityDictionary {

	doesNotUnderstand {
		arg key, value;
		key = key.asString();
		if (key.endsWith("_")) {
			key = key.keep(key.size - 1);
			this.put(key, value);
			^ this;
		} {
			key = key;
			value = this.at(key.asSymbol);
			^ value;
		};
	}

	put {
		arg key, value;
		super.put(key.asSymbol, value);
		^ this;
	}

	sortByProperty {
		arg key = \weight;
		var values = this.values.sortMap {
		arg object;
			if (object.isKindOf(Dictionary)) {
				object[\weight] ?? 0;
			} {
				if (object.respondsTo(\weight)) {
					object.weight() ?? 0;
				} {
					0;
				};
			};
		};
		^ values;
	}
}
