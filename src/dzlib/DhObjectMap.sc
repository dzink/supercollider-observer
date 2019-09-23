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
		var object;
		if (startAt.isNil.not) {
			var firstKeys, lastKeys;
			route = startAt.getRoute().asString +/+ route;
		};
		object = routes[route.asSymbol];
		if (object.isNil.not) {
			^ object;
		};
		^ nil;
	}

	traceParents {
		// arg route;
		// var routeArray = route.split($/).collect({ arg d; d.asSymbol}).asList;
		// var newroute;
		// var i;
		// while ({ (i = routeArray.indexOf('..')).isNil.not }) {
		// 	i.postln;
		// 	routeArray.removeAt(i);
		// 	routeArray.removeAt(i - 1);
		// };
		// newRoute = routeArray.join($/);
		// if (newRoute.)
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
