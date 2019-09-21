DhObjectMap : DhService {
	var <root;
	var cache;

	*new {
		^ super.new.init();
	}

	init {
		// cache = DhCache();
		super.init();
		^ this;
	}

	find {
		arg route, startAt = nil;
		var root = startAt ?? this.getRoot;
		var cacheKey = route ++ "_" ++ root.hash;
		var self = this;
		// ^ cache.cacheAside(cacheKey, {
			self.matchRoute(route, startAt);
		// });
	}

	/**
	 * Find a route based on a string. Can be cached.
	 */
	matchRoute {
		arg route, startAt;
		var root = startAt ?? this.getRoot;
		var routeKeys = this.splitAddress(route);
		var key = routeKeys.removeAt(0);
		if (key.size == 0 or: { key.asSymbol == this.getTrunk().id.asSymbol }) {
			^ this.prRoute(root, routeKeys);
		} {
			^ nil;
		};
	}

	prRoute {
		arg obj, routeKeys;
		var key = routeKeys.removeAt(0);
		if (routeKeys.size >= 0) {
			var branch = obj.findBranchById(key);
			if (branch.isKindOf(DhObject)) {
				if (routeKeys.size == 0) {
					^ branch;
				} {
					^ this.prRoute(branch, routeKeys);
				};
			} {
			 ^ nil;
			};
		};
	}

	splitAddress {
		arg key;
		var keyArray = key.asString.split($/);
		[\keyarray, keyArray].postln;
		^ keyArray.collect {
			arg key;
			key.asSymbol;
		};
	}

	idHash {
		arg object;

	}

	drawTree {
		arg depth = inf;
		^ this.prDrawTree(this.getRoot(), depth, "");
	}

	prDrawTree {
		arg obj, depth, prefix;
		(prefix ++ ": " ++ obj.id).postln;
		depth = depth - 1;
		prefix = prefix ++ "..";
		obj.getBranches().do{
			arg branch;
			this.prDrawTree(branch, depth, prefix);
		};
	}


}
