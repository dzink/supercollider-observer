DhConfigAssembler {
	var <configs;

	*new {
		var a = super.new();
		^ a.init();
	}

	init {
		configs = DhAtom();
		^ this;
	}

	getFrom {
		arg path, depth = 3, extensions = [".yml", ".yaml"];
		var candidatePaths = this.getCandidatePaths(path, depth, extensions);
		var paths = this.findConfigFiles(candidatePaths);
		this.loadConfigs(paths);
	}

	addDependencyDefaults {
		var order = this.resolveDependencyOrder();
		order.do {
			arg config;
			configs[config].addBaseDefaults(configs);
		};
	}

	getCandidatePaths {
		arg basePath, depth = 3, extensions = [".yml", ".yaml"];
		var subPaths = Array[basePath, Array(depth), Array(extensions.size)];
		var wilds = "";
		(1..depth).do {
			arg n;
			wilds = wilds ++ "/*";
			subPaths[1].add(wilds);
		};
		extensions.do {
			arg ext;
			subPaths[2].add(ext);
		};
		subPaths = subPaths.allTuples;
		subPaths = subPaths.collect({
			arg paths;
			paths.join;
		});
		^ subPaths;
	}

	findConfigFiles {
		arg candidatePaths;
		var paths = List[];
		candidatePaths.do {
			arg testPath;
			paths.addAll(testPath.pathMatch())
		};
		^ paths;
	}

	loadConfigs {
		arg paths;
		paths.do {
			arg path;
			var config;
			config = DhConfig.fromYamlFile(path);
			configs[config[\id]] = config;
		};
	}

	buildDependencyOrder {
		var edges = DhAtom();
		configs.keysValuesDo {
			arg key, config;
			var myEdges = config.keys.select({
				arg key;
				(key.asSymbol == \base or: {
					key.asString.endsWith(".base");
				});
			});
			edges[key] = myEdges;
		};
	}

	resolveDependencyOrder {
		var edges = DhAtom();
		var order = List[];
		configs.keysValuesDo {
			arg key, config;
			edges[key] = config.getBases().values;
		};
		while ({edges.size > 0}) {
			var size = edges.size;
			edges.keysValuesDo {
				arg key, myEdges;
				order.do {
					arg requirement;
					myEdges.remove(requirement);
				};
				if (myEdges.size == 0) {
					edges.removeAt(key);
					order.add(key);
				};
			};
			if (edges.size == size) {
				Exception("circular graph").throw;
			};
		};
		^ order;
	}
}
