DhObject {
	var < config;
	var <> buildConfig;
	var owner;
	var < tree;
	var root;
	var < methods;
	var address;
	var addressMap;
	var < dic;
	var <> weight = 0;
	var < tasks;


	*new {
		^ super.new().init();
	}

	init {
		tree = DhTree(this);
		tasks = DhAtom();
		^ this;
	}

	setId {
		arg anId;
		tree.setId(anId);
		^ this;
	}

	id {
		if (tree.id.isNil) {
			tree.setId = this.class.asString ++ "__" ++ this.hash.asString;
		};
		^ tree.id;
	}

	getAddress {
		if (address.isNil) {
			var trunk = this.getTrunk();
			var trunkAddress;
			if (trunk.isNil) {
				^ this.id;
			};
			trunkAddress = trunk.getAddress().asString;
			address = (trunkAddress +/+ this.id).asSymbol;
		};
		^ address;
	}

	addressMap {
		if (addressMap.isNil) {
			if (this.getTrunk.isNil.not) {
				addressMap = this.getTrunk.addressMap;
			} {
				addressMap = DhObjectMap();
				addressMap.register(this);
			};
		};
		^ addressMap;
	}

	setAddressMap {
		arg anotherAddressMap;
		addressMap = anotherAddressMap;
		^ this;
	}

	find {
		arg id;
		^ this.addressMap.find(id, this);
	}

	setDic {
		arg anotherDic;
		dic = anotherDic;
		^ this;
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
		addressMap = trunk.addressMap();
		this.addressMap.register(this);
		if (trunk.isRoot) {
			this.addressMap.register(trunk);
		};
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
			object.setAddressMap(this.addressMap());
			this.addressMap.register(object);
		};
		if (this.isRoot) {
			this.addressMap.register(this);
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

	getAllBranches {
		arg depth = inf;
		^ this.selectBranches({true}, depth);
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

	isRoot {
		^ this == this.getRoot;
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

	logcs {
		arg object, level = 10;
		this.getService(\logger).log(object.asCompileString, level);
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

	free {
		this.addressMap.removeAt(this.getAddress());
		^ super.free();
	}

	/**
	 * Clear the cached map and addressMap. Next time they are accessed, they will
	 * be regenerated.
	 */
	remap {
		this.addressMap().removeAt(this.getAddress());
		address = nil;
		addressMap = nil;
		this.getBranches().do {
			arg branch;
			branch.remap();
		};
		^ this;
	}

	notifiers {
		^ this.getService(\notifiers);
	}

	addTask {
		arg key, task;
		tasks[key] = task;
		task.setTrunk(this);
		^ this;
	}

	useConfigMethod {
		arg config;
		if (config.isNil.not) {
			if (config[\configMethod].isNil.not) {
				this.perform(config[\configMethod].asSymbol, config);
			};
		};
		^ config;
	}

}
