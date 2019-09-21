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
		order = this.resolveDependencyOrder(inProgressConfigs);
		inProgressConfigs = this.applyIncludes(inProgressConfigs, order);
		inProgressConfigs = this.addDependencyBases(inProgressConfigs, order);

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
			var basePath;
			config = DhConfig.fromYamlFile(path);
			config[\configBasePath] = path.dirname;
			inProgressConfigs[config[\id]] = config;
		};
		^ inProgressConfigs;
	}

	applyIncludes {
		arg inProgressConfigs, order;
		order.do {
			arg key;
			inProgressConfigs[key].applyIncludes();
		};
		^ inProgressConfigs;
	}

	/**
	 * Add dependency defaults.
	 * Anywhere a config has a `base` key, that key's parent should have all the
	 * objects of the config with that base imported as defaults.
	 */
	addDependencyBases {
		arg inProgressConfigs, order;
		order.do {
			arg config;
			inProgressConfigs[config].addBaseDefaults(inProgressConfigs);
		};
		^ inProgressConfigs;
	}

	/**
	 * Return an array of configs in the order of which took the fewest iterations
	 * to resolve bases.
	 */
	resolveDependencyOrder {
		arg inProgressConfigs;
		var edges = this.getDependencyEdges(inProgressConfigs);
		var completed = List[];

		while ({edges.size > 0}) {
			var size = edges.size;
			edges.keysValuesDo {
				arg key, myEdges;
				completed.do {
					arg requirement;
					myEdges.remove(requirement);
				};
				if (myEdges.size == 0) {
					edges.removeAt(key);
					completed.add(key);
				};
			};

			// If the size is the same, you've got a circular graph.
			if (edges.size == size) {
				Exception("circular graph").throw;
			};
		};
		^ completed;
	}

	getDependencyEdges {
		arg inProgressConfigs;
		var edges = DhAtom();
		inProgressConfigs.keysValuesDo {
			arg key, config;
			var bases = config.getBaseKeys();
			edges[key] = bases.collect({
				arg base;
				config[base].asSymbol;
			});
		};
		^ edges;
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
