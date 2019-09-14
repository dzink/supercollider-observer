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

	branch {
		arg ... branch;
		if (branch.isKindOf(Array).not) {
			branch = [branch];
		};
		branch.do {
			arg b;
			this.addBranch(b);
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

	addBranch {
		arg branch;
		if (branches.isNil) {
			branches = List[];
		};
		branches.add(branch);
		^ this;
	}

	selfRespondsTo {
		arg method;
		^ self.respondsTo(method.asSymbol());
	}

	/**
	 * Performs a method on its self object.
	 */
	selfPerformOn {
		arg method ... args;
		var result = nil;
		if (this.selfRespondsTo(method)) {
			result = self.perform(method, *(args));
		};
		^ result;
	}

	/**
	 * Goes down the trunk until it finds a trunk that can perform the action.
	 */
	trunkPerformOn {
		arg method ... args;
		if (trunk.isNil) {
			^ nil;
		};
		if (trunk.selfRespondsTo(method)) {
			^ trunk.selfPerformOn(method, *(args));
		} {
			^ trunk.trunkPerformOn(method, *(args));
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
		if (trunk.selfRespondsTo(\at)) {
			var v;
			v = trunk.selfPerformOn(\at, key);
			if (v.isNil.not) {
				^ v;
			} {
				^ trunk.trunkProperty(key);
			};
		} {
			^ trunk.trunkProperty(key);
		};
	}
}
