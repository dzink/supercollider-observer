DhConfigAssembler {
	var <configs;
	var <>compilePath = "./compiled";
	var <>sourcePath = "./src";
	var <>sourceDepth = 5;
	var <>extensions;

	*new {
		var a = super.new();
		^ a.init();
	}

	init {
		extensions = [".yml", ".yaml"];
		^ this;
	}

	assemble {
		var inProgressConfigs, order, candidatePaths, paths;
		inProgressConfigs = DhAtom();
		paths = this.findConfigFiles(candidatePaths);
		inProgressConfigs = this.loadConfigsPaths(inProgressConfigs, paths);
		inProgressConfigs = this.addDependencyBases(inProgressConfigs);
		configs = inProgressConfigs;
		^ inProgressConfigs;
	}

	findConfigFiles {
		var candidatePaths = this.getCandidatePaths();
		var paths = List[];
		candidatePaths.do {
			arg testPath;
			paths.addAll(testPath.pathMatch())
		};
		^ paths;
	}

	getCandidatePaths {
		var subPaths = Array[sourcePath, Array(sourceDepth), Array(extensions.size)];
		var wilds = "";
		(1..sourceDepth).do {
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

	loadConfigsPaths {
		arg inProgressConfigs, paths;
		paths.do {
			arg path;
			var config;
			config = DhConfig.fromYamlFile(path);
			inProgressConfigs[config[\id]] = config;
		};
		^ inProgressConfigs;
	}

	/**
	 * Add dependency defaults.
	 * Anywhere a config has a `base` key, that key's parent should have all the
	 * objects of the config with that base imported as defaults.
	 */
	addDependencyBases {
		arg inProgressConfigs;
		var order = this.resolveDependencyOrder(inProgressConfigs);
		order.do {
			arg config;
			inProgressConfigs[config].addBaseDefaults(inProgressConfigs);
		};
		^ inProgressConfigs;
	}

	resolveDependencyOrder {
		arg inProgressConfigs;
		var edges = DhAtom();
		var order = List[];
		inProgressConfigs.keysValuesDo {
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

	saveCompiled {
		arg path;
		configs.keysValuesDo {
			arg key, config;
			var path = compilePath +/+ config[\id] ++ ".sc";
			var data = config.asCompileString();
			File.use(path, "w", {
				arg file;
				file.write(config.asCompileString);
			});
		};
	}
}
