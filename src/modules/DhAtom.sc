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

	// method {
	// 	arg method ... args;
	// 	var result;
	// 	if (this.respondsTo(method)) {
	// 		result = this.perform(method, **args);
	// 	};
	// 	^ result;
	// }
}
