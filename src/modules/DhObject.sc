DhObject {
	var config;
	var owner;
	var id;
	var <tree;

	*new {
		^ super.new().init();
	}

	init {
		tree = DhTree(this);
		^ this;
	}

	storeConfig {
		arg aConfig;
		config = aConfig;
		^ this;
	}

	setOwner {
		arg anOwner;
		^ this.branchFrom(anOwner);
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

	getBranches {
		var branches = tree.branchList;
		^ branches.collect({
			arg self;
			if (self.isNil) { nil } { [\self, self, self.getSelf(), self.getSelf().id].postln; self.getSelf() };
		});
	}

	selectBranches {
		arg select = nil, depth = 1;
		var branches = tree.getBranches(select, depth);
		^ branches.collect({
			arg self;
			if (self.isNil) { nil } { [\self, self, self.getSelf(), self.getSelf().id].postln; self.getSelf() };
		});
	}

	getChildren {
		arg select;
		^ this.getBranches(select);
	}

	/**
	 * Sets the trunk.
	 */
	setTrunk {
		arg trunk;
		tree.setTrunk(trunk);
	}

	getTrunk {
		var self = tree.getTrunk();
		^ if (self.isNil) { nil } { self.getSelf() };
	}

	setId {
		arg anId;
		id = anId;
		^ this;
	}

	id {
		if (id.isNil) {
			id = this.class.asString ++ "_" ++ this.hash.asString;
		};
		^ id;
	}
}
