DhAtom : IdentityDictionary {

	doesNotUnderstand {
		arg key, value;
		key = key.asString();
		if (key.endsWith("_")) {
			key = key.keep(key.size - 1);
			this.[key.asSymbol] = value;
			^ this;
		} {
			key = key;
			value = this.at(key.asSymbol);
			^ value;
		};
	}

	sortByProperty {
		arg key = \weight;
		var values = this.values.sortMap {
			if (this.isKindOf(Dictionary)) {
				this[\weight] ?! 0;
			} {
				0;
			};
		};
		^ values;
	}
	// method {
	// 	arg method ... args;
	// 	var result;
	// 	if (this.respondsTo(method)) {
	// 		result = this.perform(method, **args);
	// 	};
	// 	^ result;
	// }
}
