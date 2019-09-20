DhObject {
	var config;
	var owner;
	var id;
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
		id = anId;
		^ this;
	}

	id {
		if (id.isNil) {
			id = this.class.asString ++ "__" ++ this.hash.asString;
		};
		^ id;
	}

	storeConfig {
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

	getTrunk {
		^ DhTree.asSelf(tree.getTrunk());
	}

	getTrunks {
		arg depth = inf;
		var trunks = tree.selectTrunks({true}, depth);
		^ DhTree.asSelves(trunks);
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
}
