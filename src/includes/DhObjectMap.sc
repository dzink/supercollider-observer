DhObjectMap {
	var routes;

	*new {
		^ super.new.init();
	}

	init {
		// cache = DhCache();
		routes = IdentityDictionary();
		// super.init();
		^ this;
	}

	find {
		arg route, startAt = nil;
		if (startAt.isNil.not) {
			var firstKeys, lastKeys;
			route = startAt.getRoute().asString +/+ route;
		};
		^ routes[route.asSymbol];
	}

	register {
		arg object;
		routes[object.getRoute().asSymbol] = object;
		^ this;
	}

	removeAt {
		arg key;
		routes.removeAt(key.asSymbol);
		^ this;
	}

	includes {
		arg key;
		^ routes.includes(key.asSymbol);
	}

	list {
		arg wildcard;
		// @TODO use wildcard.
		^ routes.keys;
	}
}
