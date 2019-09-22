DhObject {
	var < config;
	var owner;
	// var id;
	var < tree;
	var root;
	var < methods;
	var route;
	var routeMap;

	*new {
		^ super.new().init();
	}

	init {
		tree = DhTree(this);
		^ this;
	}

	setId {
		arg anId;
		tree.setId(anId);
		^ this;
	}

	id {
	 ^ tree.id;
		// if (id.isNil) {
		// 	id = this.class.asString ++ "__" ++ this.hash.asString;
		// };
		// ^ id;
	}

	getRoute {
		if (route.isNil) {
			var trunks = this.getTrunks();
			trunks = trunks.reverse.collect({
				arg trunk;
				trunk.id;
			});
			trunks = trunks.add(this.id);
			route = trunks.join("/").asSymbol;
		};
		^ route;
	}

	setConfig {
		arg aConfig;
		config = aConfig;
		^ this;
	}

	/**
	 * Sets the trunk.
	 */
	setTrunk {
		arg trunk;
		tree.setTrunk(trunk);
		routeMap = trunk.getRouteMap();
		routeMap.register(this);
		^ this;
	}

	/**
	 * Add a single or multiple branches. The tree determines if it is plural or
	 * not;
	 */
	addBranches {
		arg ... objects;
		tree.addBranches(objects);
		objects.asArray.do {
			arg object;
			object.setRouteMap(this.getRouteMap());
			routeMap.register(object);
		};
		^ this;
	}

	/**
	 * Gets a list of this objects branch items.
	 */
	getBranches {
		var branches = tree.getBranches;
		^ DhTree.asSelves(branches);
	}

	/**
	 * Filter a list of this object's descendant branches.
	 */
	selectBranches {
		arg select = nil, depth = inf;
		var branches = tree.selectBranches(select, depth);
		^ DhTree.asSelves(branches);
	}

	findBranchById {
		arg id;
		var branches = this.selectBranches({
			arg branch;
			branch.id.asSymbol == id.asSymbol;
		}, 1);
		if (branches.size > 0) {
			^ branches.at(0);
		};
		^ nil;
	}

	getTrunk {
		^ DhTree.asSelf(tree.getTrunk());
	}

	getTrunks {
		arg depth = inf;
		^ this.selectTrunks({true}, depth);
	}

	selectTrunks {
		arg select, depth = inf;
		var trunks = tree.selectTrunks(select, depth);
		^ DhTree.asSelves(trunks);
	}

	/**
	 * Only return the first trunk up the tree that returns true.
	 */
	selectTrunkWhere {
		arg select, depth = inf;
		var trunks;
		var newselect = {
			arg trunk;
			if (select.value(trunk).asBoolean) { \return } { false };
		};
		trunks = tree.selectTrunks(newselect, depth);
		^ DhTree.asSelves(trunks);
	}

	getRoot {
		if (root.isNil) {
			root = tree.getRoot.asSelf;
		};
		^ root;
	}

	/**
	 * Reset the root for this and all parents.
	 */
	resetRoot {
		root = nil;
		^ this;
	}

	asTree {
		^ tree;
	}

	configure {
		^ this;
	}

	getService {
		arg key;
		^ this.prInheritService(key);
	}

	hasService {
		^ false;
	}

	prInheritService {
		arg key;
		var trunks;

		// @TODO this should look up the tree as well.

		trunks = this.selectTrunkWhere({
			arg trunk;
			trunk.hasService(key);
		});
		if (trunks.size > 0) {
			^trunks[0].getService(key);
		};
		("Service " ++ key ++ " not found on plugin " ++ this.id).error;
		^ nil;
	}

	log {
		arg message, level = 10;
		this.getService(\logger).log(message, level);
		^ this;
	}

	warn {
		arg message;
		this.getService(\logger).warn(message);
		^ this;
	}

	error {
		arg message;
		this.getService(\logger).error(message);
		^ this;
	}

	getRouteMap {
		if (routeMap.isNil) {
			if (this.getTrunk().isNil) {
				routeMap = DhObjectMap();
				routeMap.register(this);
			} {
				^ this.root(routeMap);
			};
		};
		^ routeMap;
	}

	setRouteMap {
		arg map;
		routeMap = map;
		^ this;
	}

	findByRoute {
		arg route;
		^ this.getRouteMap.find(route);
	}

	findByRouteFrom {
		arg route, startAt;
		^ this.getRouteMap.find(route, startAt);
	}

	free {
		this.getRouteMap.removeAt(this.getRoute());
		^ super.free();
	}

	/**
	 * Clear the cached map and routeMap. Next time they are accessed, they will
	 * be regenerated.
	 */
	remap {
		this.getRouteMap().removeAt(this.getRoute());
		route = nil;
		routeMap = nil;
		this.getBranches().do {
			arg branch;
			branch.remap();
		};
		^ this;
	}

}
