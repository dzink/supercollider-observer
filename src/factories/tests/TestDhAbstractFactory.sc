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
		f.configs = configs;
		// configs[\test].postcs;
		f.build(configs[\test]);
		// [\dddd, f.builtMembers].postcs;
		f.builtMembers.do {
			arg member;
			member.class.postln;
		};
		f.builtMembers.size.postln;
	}
}
