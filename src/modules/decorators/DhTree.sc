DhTree {
	var <trunk;
	var <>self;
	var <branches;
	var <root;

	*new {
		arg self;
		var t = super.new();
		^ t.init(self);
	}

	init {
		arg aSelf;
		self = aSelf;
		^ this;
	}

	/**
	 * Add a group of branches to this.
	 */
	branch {
		arg ... branch;
		if (branch.isKindOf(Array).not) {
			branch = [branch];
		};
		branch.do {
			arg b;
			this.prAddBranch(b);
			b.setTrunk(this);
		}
		^ this;
	}

	setTrunk {
		arg aTrunk;
		trunk = aTrunk;
		root = this.getRoot();
		^ this;
	}

	getRoot {
		if (trunk.isNil) {
			^ this;
		} {
			^ trunk.getRoot();
		};
	}

	prAddBranch {
		arg branch;
		if (branches.isNil) {
			branches = List[];
		};
		branches.add(branch);
		^ this;
	}

	prSelfRespondsTo {
		arg method;
		^ self.respondsTo(method.asSymbol());
	}

	/**
	 * Performs a method on its self object.
	 */
	prSelfPerform {
		arg method ... args;
		var result = nil;
		if (this.prSelfRespondsTo(method)) {
			result = self.perform(method, *(args));
		};
		^ result;
	}

	/**
	 * Goes down the trunk until it finds a trunk that can perform the action.
	 */
	trunkPerform {
		arg method ... args;
		if (trunk.isNil) {
			^ nil;
		};
		if (trunk.prSelfRespondsTo(method)) {
			^ trunk.prSelfPerform(method, *(args));
		} {
			^ trunk.trunkPerform(method, *(args));
		};
	}

	/**
	 * Goes down the trunk until it finds a trunk that has the non-nil property.
	 */
	trunkProperty {
		arg key;
		if (trunk.isNil) {
			^ nil;
		};
		if (trunk.prSelfRespondsTo(\at)) {
			var v;
			v = trunk.prSelfPerform(\at, key);
			if (v.isNil.not) {
				^ v;
			} {
				^ trunk.trunkProperty(key);
			};
		} {
			^ trunk.trunkProperty(key);
		};
	}

	trunkFilter {
		arg func = {};
		var t = this.prTrunkFilter(func);
		if (t.isKindOf(DhTree)) {
			^ t.self;
		};
		^ nil;
	}

	/**
	 * Finds the first trunk that matches the given filter.
	 */
	prTrunkFilter {
		arg func = {};
		if (trunk.isNil) {
			^ nil;
		} {
			if (func.(trunk.self)) {
				^ trunk;
			} {
				^ trunk.prTrunkFilter(func);
			};
		};
	}

	/**
	 * Try to turn this into a tree if possible.
	 */
	*asTree {
		arg tree;
		if (tree.isKindOf(DhTree)) {
			^ tree;
		};
		if (tree.respondsTo(\at) and: {tree[\tree].isNil.not}) {
			^ tree[\tree];
		};
		if (tree.respondsTo(\tree)) {
			^ tree.tree();
		};
		^ nil;
	}
}
