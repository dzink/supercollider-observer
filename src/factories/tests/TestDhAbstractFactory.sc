TestDhAbstractFactory : TestDh {
	var f;
	var a;

	setUp {
		f = DhAbstractFactory();
		a = DhConfigAssembler();
		a.compilePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/compiled/";
		a.sourcePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/";
	}

	tearDown {
		f.free;
		a.free;
	}

	test_factory {
		var configs = a.assemble();
		var root;
		f.configs = configs;
		root = f.build(configs[\test]);
		f.builtMembers.do {
			arg member;
		};
	}
}
