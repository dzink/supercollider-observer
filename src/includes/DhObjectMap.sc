DhObjectMap {
	var addresses;

	*new {
		^ super.new.init();
	}

	init {
		// cache = DhCache();
		addresses = IdentityDictionary();
		// super.init();
		^ this;
	}

	find {
		arg route, startAt = nil;
		if (startAt.isNil.not) {
			var firstKeys, lastKeys;
			route = startAt.getAddress().asString +/+ route;
		};
		^ addresses[route.asSymbol];
	}

	register {
		arg object;
		addresses[object.getAddress().asSymbol] = object;
		^ this;
	}

	removeAt {
		arg key;
		addresses.removeAt(key.asSymbol);
		^ this;
	}

	includes {
		arg key;
		^ addresses.includes(key.asSymbol);
	}

	list {
		arg wildcard;
		// @TODO use wildcard.
		^ addresses.keys;
	}
}
