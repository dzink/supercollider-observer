TestDhConfigAssembler : TestDh {
	var a;

	setUp {
		a = DhConfigAssembler();
		a.compilePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/compiled/";
		a.sourcePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/";
	}

	tearDown {
		a.free;
	}

	test_getCandidatePaths {
		// var paths = a.getCandidatePaths("/core", 3, [".yaml", ".yml"]);
		// paths = paths.collect(_.asSymbol);
		// this.assert(paths.includes("/core/*.yaml".asSymbol), "\"/core/*.yaml\" is added.");
		// this.assert(paths.includes("/core/*/*.yml".asSymbol), "\"/core/*/*.yml\" is added.");
		// this.assert(paths.includes("/core/*/*/*.yaml".asSymbol), "\"/core/*/*/*.yaml\" is added.");
	}

	test_findConfigFiles {
		// var paths = a.findConfigFiles(a.getCandidatePaths());
	}

	test_loadConfigs {
		// a.getFrom("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/");
		// a.addDependencyDefaults();
		var configs = a.assemble();
		// a.configs.postcs;
		a.saveCompiled(configs);
	}

	test_saveCompiled {
		// var save_path =
		// a.getFrom("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/");
		// a.addDependencyDefaults();
		var configs = a.assemble();
		this.assert(configs["core:service:server"].includesKey("serverOptions.sampleRate"), "Config imports sampleRate");
		this.assert(configs["core:service:stereo_server"].includesKey("serverOptions.sampleRate"), "Stereo config imports sampleRate");

	}

	// test_loadConfigs {
	// 	a.getFrom("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/");
	// 	// a.
	// }
}
