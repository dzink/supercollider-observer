TestDhConfigAssembler : TestDh {
	var a;

	setUp {
		a = DhConfigAssembler();
	}

	tearDown {
		a.free;
	}

	test_getCandidatePaths {
		var paths = a.getCandidatePaths("/core", 3, [".yaml", ".yml"]);
		paths = paths.collect(_.asSymbol);
		this.assert(paths.includes("/core/*.yaml".asSymbol), "\"/core/*.yaml\" is added.");
		this.assert(paths.includes("/core/*/*.yml".asSymbol), "\"/core/*/*.yml\" is added.");
		this.assert(paths.includes("/core/*/*/*.yaml".asSymbol), "\"/core/*/*/*.yaml\" is added.");
	}

	test_findConfigFiles {
		var paths = a.findConfigFiles(a.getCandidatePaths("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/"));
	}

	test_loadConfigs {
		a.getFrom("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/");
		a.addDependencyDefaults();
		a.configs.keysValuesDo {
			arg key, value;
			[key, value.keys].postcs;
		};
	}

	// test_loadConfigs {
	// 	a.getFrom("/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/");
	// 	// a.
	// }
}
