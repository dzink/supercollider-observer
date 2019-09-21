DhObject {
	var < config;
	var owner;
	// var id;
	var < tree;
	var root;
	var < methods;

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

	address {
		var trunks = this.getTrunks();
		trunks = trunks.reverse.collect({
			arg trunk;
			trunk.id;
		});
		trunks = trunks.add(this.id);
		^ trunks.join("/");
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
	}

	/**
	 * Add a single or multiple branches. The tree determines if it is plural or
	 * not;
	 */
	addBranches {
		arg ... objects;
		tree.addBranches(objects);
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


}
