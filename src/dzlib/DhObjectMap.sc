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
		if ({ (object = routes[route.asSymbol]).isNil.not }) {
			^ object;
		};
		// var a = "a/b/c/d/e/../../../meow/../mix".split($/).collect({ arg d; d.asSymbol}).asList;

var i;
while ({ (i = a.indexOf('..')).isNil.not }) {
	i.postln;
	a.removeAt(i);
	a.removeAt(i - 1);

};
	}

	traceParents {
		arg route;
		var routeArray = route.split($/).collect({ arg d; d.asSymbol}).asList;
		var newroute;
		var i;
		while ({ (i = routeArray.indexOf('..')).isNil.not }) {
			i.postln;
			routeArray.removeAt(i);
			routeArray.removeAt(i - 1);
		};
		newRoute = routeArray.join($/);
		if (newRoute.)
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
