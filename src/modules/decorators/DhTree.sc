DhTree {
	var trunk;
	var self;
	var <branchList;
	var <root;
	var id;

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

	setSelf {
		arg aSelf;
		self = aSelf;
		^ this;
	}


	setTrunk {
		arg aTrunk;
		if (aTrunk.isKindOf(DhTree).not) {
			aTrunk = aTrunk.tree;
		};
		if (trunk != aTrunk) {
		trunk = aTrunk;
		if (trunk.branchList.includes(this).not) {
			trunk.addBranch(this);
		};
		}
		^ this;
	}

	/**
	 * Add branches.
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
				[\itsabranch, branch.id].postln;
			};
			if (branch.getTrunk != this) {
				branch.setTrunk(this);
			};
		};
		^ this;
	}

	addBranch {
		arg branch;
		^ this.addBranches([branch]);
	}

	getSelf {
		^ self;
	}

	getTrunk {
		^ trunk;
	}

	/**
	 * Get the subbranches of this branch.
	 * @param Integer depth
	 *   How many levels deep to search.
	 * @param Function select
	 *   A function that returns true if the branch should be selected, nil if the
	 *   branch should not be selected, and false if the branch should not be
	 *   selected and the search should not review any branches of that branch.
	 */
	getBranches {
		arg select = nil, depth = 1;
		var list = List();
		^ this.prGetBranches(list, select, depth);
	}

	/**
	 * Only get first level of branches.
	 */
	getChildren {
		arg select = nil;
		^ this.getBranches(select, 1);
	}

	/**
	 * Get branches on the same level as this one.
	 */
	getSiblings {
		arg select = nil;
		var aTrunk = this.trunk;
		if (aTrunk.isKindOf(DhTree)) {
			^ trunk.getChildren();
		} {
			^ nil
		};
	}

	/**
	 * Get all trunks of this branch.
	 * @param Function select
	 *   A function that returns true if the trunk should be selected, nil if the
	 *   trunk should not be selected, and false if the trunk should not be
	 *   selected and the search should not look further.
	 */
	getTrunks {
		arg select = nil, depth = inf;
		var list = DhAtom();
		^ this.prGetTrunks(list, select, depth);
	}

	/**
	 * Keep getting trunks until you cannot trunk anymore.
	 * Cache, because this seems like a lot of work.
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

	prGetTrunks {
		arg list, select, depth;
		var result = select.value(self, this);
		if (result != false or: { list.includes(this) }) {
			if (result.isNil.not) {
				list.put(id, this);
			};
			depth = depth - 1;
			if (trunk.isNil.not and: { depth > 0 }) {
				list = trunk.prGetTrunks(list, select, depth);
			};
		};
		^ list;
	}



	prGetBranches {
		arg list, select, depth;
		depth = depth - 1;
		branchList.do {
			arg branch;
			var result = if (select.isNil) {
				true;
			} {
				select.value(self, this);
			};
			[\gottenbranch, this.id].postln;
			if (result != false or: { list.includes(this) }) {
				if (result.isNil.not) {
					list.add(this);
				};
				[\depthis, depth].postln;

				if (depth > 0) {
					branchList.do {
						arg branch;
						list = branch.prGetBranches(list, select, depth);
					};
				};
			};
		}

		^ list;
	}

	prGetSelves {
		arg list;
		^ list.collect({
			arg branch;
			branch.self;
		});
	}

	id {
		if (id.isNil) {
			id = this.class.asString ++ "_" ++ this.hash.asString;
		};
		^ id;
	}
	//
	// /**
	//  * Add a group of branches to this.
	//  */
	// branch {
	// 	arg ... branch;
	// 	if (branch.isKindOf(Array).not) {
	// 		branch = [branch];
	// 	};
	// 	branch.do {
	// 		arg b;
	// 		this.prAddBranch(b);
	// 		b.setTrunk(this);
	// 	}
	// 	^ this;
	// }
	//
	// setTrunk {
	// 	arg aTrunk;
	// 	trunk = aTrunk;
	// 	root = this.getRoot();
	// 	^ this;
	// }
	//
	// getRoot {
	// 	if (trunk.isNil) {
	// 		^ this;
	// 	} {
	// 		^ trunk.getRoot();
	// 	};
	// }
	//
	// prAddBranch {
	// 	arg branch;
	// 	if (branches.isNil) {
	// 		branches = List[];
	// 	};
	// 	branchList.add(branch);
	// 	^ this;
	// }
	//
	// prSelfRespondsTo {
	// 	arg method;
	// 	^ self.respondsTo(method.asSymbol());
	// }
	//
	// /**
	//  * Performs a method on its self object.
	//  */
	// prSelfPerform {
	// 	arg method ... args;
	// 	var result = nil;
	// 	if (this.prSelfRespondsTo(method)) {
	// 		result = self.perform(method, *(args));
	// 	};
	// 	^ result;
	// }
	//
	// /**
	//  * Performs a method on its self object.
	//  */
	// prSelfFunction {
	// 	arg function;
	// 	var ^ function.value(self);
	// }
	//
	// /**
	//  * Goes down the trunk until it finds a trunk that can perform the action.
	//  */
	// trunkPerform {
	// 	arg method ... args;
	// 	if (trunk.isNil) {
	// 		^ nil;
	// 	};
	// 	if (trunk.prSelfRespondsTo(method)) {
	// 		^ trunk.prSelfPerform(method, *(args));
	// 	} {
	// 		^ trunk.trunkPerform(method, *(args));
	// 	};
	// }
	//
	// trunkSelect {
	// 	arg
	// }
	//
	// trunkSelect {
	// 	arg func;
	// 	var result;
	// 	if (trunk.isNil) {
	// 		^ nil;
	// 	};
	// 	result = this.
	// 	if (trunk.prSelfRespondsTo(method)) {
	// 		^ trunk.prSelfPerform(method, *(args));
	// 	} {
	// 		^ trunk.trunkPerform(method, *(args));
	// 	};
	// }
	//
	//
	// /**
	//  * Goes down the trunk until it finds a trunk that has the non-nil property.
	//  */
	// trunkProperty {
	// 	arg key;
	// 	if (trunk.isNil) {
	// 		^ nil;
	// 	};
	// 	if (trunk.prSelfRespondsTo(\at)) {
	// 		var v;
	// 		v = trunk.prSelfPerform(\at, key);
	// 		if (v.isNil.not) {
	// 			^ v;
	// 		} {
	// 			^ trunk.trunkProperty(key);
	// 		};
	// 	} {
	// 		^ trunk.trunkProperty(key);
	// 	};
	// }
	//
	// /**
	//  * Return all trunks.
	//  */
	// trunks {
	//
	// }
	//
	// /**
	//  * Return a dictionary of all trunks.
	//  */
	// trunksKeysValues {
	//
	// }
	//
	// trunkFilter {
	// 	arg func = {};
	// 	var t = this.prTrunkFilter(func);
	// 	if (t.isKindOf(DhTree)) {
	// 		^ t.self;
	// 	};
	// 	^ nil;
	// }
	//
	// /**
	//  * Finds the first trunk that matches the given filter.
	//  */
	// prTrunkFilter {
	// 	arg func = {};
	// 	if (trunk.isNil) {
	// 		^ nil;
	// 	} {
	// 		if (func.(trunk.self)) {
	// 			^ trunk;
	// 		} {
	// 			^ trunk.prTrunkFilter(func);
	// 		};
	// 	};
	// }
	//
	// /**
	//  * Try to turn this into a tree if possible.
	//  */
	// *asTree {
	// 	arg tree;
	// 	if (tree.isKindOf(DhTree)) {
	// 		^ tree;
	// 	};
	// 	if (tree.respondsTo(\at) and: {tree[\tree].isNil.not}) {
	// 		^ tree[\tree];
	// 	};
	// 	if (tree.respondsTo(\tree)) {
	// 		^ tree.tree();
	// 	};
	// 	^ nil;
	// }
}
