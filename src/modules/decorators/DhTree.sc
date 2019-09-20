DhTree {
	var trunk;
	var self;
	var <branchList;
	var <root;
	var id;

	classvar < c_SELECT_ADD = 1;
	classvar < c_SELECT_DO_NOT_ADD = 2;
	classvar < c_SELECT_END = 3;
	classvar < c_SELECT_ADD_AND_END = 3;

	*new {
		arg self, id;
		var t = super.new();
		^ t.init(self, id);
	}

	init {
		arg aSelf, anId;
		self = aSelf;
		id = (anId ?? this.id).asSymbol;
		branchList = List();
		^ this;
	}

	id {
		if (id.isNil) {
			id = this.class.asString ++ "_" ++ this.hash.asString;
		};
		^ id;
	}

	setSelf {
		arg aSelf;
		self = aSelf;
		^ this;
	}

	getSelf {
		^ self;
	}

	getTrunk {
		^ trunk;
	}

	// Set heirarchy

	/**
	 * Sets the trunk of this tree node. Also adds this as a branch of its trunk.
	 */
	setTrunk {
		arg aTrunk;
		if (aTrunk.isKindOf(DhTree).not) {
			aTrunk = aTrunk.tree;
		};
		if (trunk != aTrunk) {
		trunk = aTrunk;
		if (trunk.branchList.includes(this).not) {
			trunk.addBranches([this]);
		};
		}
		^ this;
	}

	/**
	 * Add branches. Also adds this as the trunk of each branch.
	 * @param Array branches
	 *   An array of branches. Passing in a single branch will wrap it in an
	 *   array.
	 */
	addBranches {
		arg branches;
		if (branches.isKindOf(Array).not) {
			branches = [branches];
		};
		branches.do {
			arg branch;
			if (branch.isKindOf(DhTree).not) {
				branch = branch.tree;
			};
			if (branchList.includes(branch).not) {
				branchList = branchList.add(branch);
			};
			if (branch.getTrunk != this) {
				branch.setTrunk(this);
			};
		};
		^ this;
	}

	/**
	 * Get this trunk's direct branches.
	 */
	getBranches {
		^ branchList;
	}

	/**
	 * Get all descendant branches of this branch.
	 * @param Function select
	 *   A function that returns true if the branch should be selected, nil if the
	 *   branch should not be selected, and false if the branch should not be
	 *   selected and the search should not review any branches of that branch.
	 * @param Integer depth
	 *   How many levels deep to search.
	 */
	selectBranches {
		arg select = nil, depth = inf;
		var list = List();
		if (select.isNil) {
			select = true;
		};
		^ this.prSelectBranches(list, select, depth);
	}

	/**
	 * Get all ancestor branches of this branch.
	 * @param Function select
	 *   A function that returns true if the branch should be selected, nil if the
	 *   branch should not be selected, and false if the branch should not be
	 *   selected and the search should not review any branches of that branch.
	 * @param Integer depth
	 *   How many levels deep to search.
	 */
	selectTrunks {
		arg select = nil, depth = inf;
		var list = List();
		if (select.isNil) {
			select = true;
		};
		^ this.prSelectTrunks(list, select, depth);
	}

	/**
	 * Keep getting trunks until you cannot trunk anymore.
	 * Store the result, because this seems like a lot of work.
	 */
	getRoot {
		if (root.isNil) {
			var aRoot;
			aRoot = this.getTrunks({
				arg s;
				if (s.tree.trunk.isNil) { true } { nil };
			});
			if (aRoot.size > 0) {
				root = aRoot.values[0];
			} {
				root = nil;
			};
		};
		^ root;
	}

	/**
	 * Probably never need this but here it is.
	 */
	resetRoot {
		root = nil;
		^ this.getRoot();
	}

	/**
	 * Recursively select from all ancestor trunks, based on a filter.
	 */
	prSelectTrunks {
		arg list, select, depth;
		depth = depth - 1;
		if (trunk.isNil.not and: { list.includes(trunk).not }) {
			var selectCompare = DhTree.prSelectResult(trunk, select);
			if (selectCompare != c_SELECT_END) {
				if (selectCompare == c_SELECT_ADD) {
					list = list.add(trunk);
				};
				if (depth > 0) {
					list = trunk.getTrunk(list, select, depth);
				};
			};
		^ list;
		}

	}

	/**
	 * Recursively select from all descendant branches, based on a filter.
	 */
	prSelectBranches {
		arg list, select, depth;
		depth = depth - 1;
		branchList.do {
			arg branch;
			if (branch.isNil.not and: {list.includes(branch).not}) {
				var selectCompare = DhTree.prSelectResult(branch, select);
				if (selectCompare != c_SELECT_END) {
					if (selectCompare == c_SELECT_ADD) {
						list = list.add(branch);
					};
					if (depth > 0) {
						list = branch.prSelectBranches(list, select, depth);
					};
				};
			};

		};
		^ list;
	}

	/**
	 * Get the result of a select function performed on a branch.
	 * Uses named classvars to simplify readability.
	 */
	*prSelectResult {
		arg branch, select;
		var result;
		if (select.isNil) {
			^ c_SELECT_ADD;
		};
		result = (select.value(branch.getSelf()));
		if (result == \end) {
			^ c_SELECT_END;
		};
		if (result.isNil or: { result == false }) {
			^ c_SELECT_DO_NOT_ADD;
		};
		^ c_SELECT_ADD;
	}

	*asSelves {
		arg list;
		^ list.asArray.collect({
			arg branch;
			DhTree.asSelf(branch);
		}).reject({
			arg object;
			object.isNil;
		});
	}

	asSelves {
		arg list;
		^ DhTree.asSelves(list);
	}

	*asSelf {
		arg branch;
		branch = branch ?? this;
		^ if (branch.isKindOf(DhTree)) { branch.asSelf } { nil };
	}

	asSelf {
		^ self;
	}

	asTree {
		^ this;
	}
}
