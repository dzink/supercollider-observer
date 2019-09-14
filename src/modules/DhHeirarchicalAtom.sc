/**
 * A DhAtom that is part of a hierarchy.
 */
DhHeirarchicalAtom : DhAtom {
	var <tree;

	*new {
		var t = super.new();
		^ t.init();
	}

	init {
		tree = DhTree(this);
		^ this;
	}

	branch {
		arg ... branches;
		if (branches.isKindOf(Array).not) {
			branches = [branches];
		};
		branches.do {
			arg branch;
			tree.branch(branch.tree);
		};
		// branch = branch.collect {
		// 	arg b;
		// 	b.tree;
		// };
		// tree.branch(*(branch));
		^ this;
	}

	setTrunk {
		arg trunk;
		tree.setTrunk(trunk);
		^ this;
	}

	/**
	 * Perform a method on the trunk, or further down towards the root.
	 */
	trunkPerformOn {
		arg method ... args;
		^ tree.trunkPerformOn(method, *(args));
	}

	/**
	 * Get a property of the trunk, or further down towards the root.
	 */
	trunkProperty {
		arg key;
		^ tree.trunkProperty(key);
	}

	/**
	 * Get the root object.
	 */
	root {
		^ tree.root.self;
	}

	/**
	 * Get the trunk object.
	 */
	trunk {
		if (tree.trunk.isNil) {
			^ nil;
		} {
			^ tree.trunk.self;
		};
	}

	/**
	 * Get a List of branch objects.
	 */
	branches {
		^ tree.branches.collect {
			arg branch;
			branch.self;
		};
	}
}
