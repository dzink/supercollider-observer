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
