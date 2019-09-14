TestDhTree : TestDh {
	var p, t, c1, c2;

	setUp {
		p = DhTree(Object());
		t = DhTree(Object());
		c1 = DhTree(Object());
		c2 = DhTree(Object());
		p.branch(t);
		t.branch(c1, c2);
	}

	tearDown {
		p.free;
		t.free;
		c1.free;
		c2.free;
	}

	test_trunk {
		this.assertEquals(t.trunk, p, "P is trunk to T.");
		this.assertEquals(c1.trunk, t, "T is trunk to c1.");
		this.assertEquals(c2.trunk, t, "T is trunk to c2.");
	}

	test_branches {
		this.assert(p.branches.includes(t), "T is a branch to P");
		this.assert(t.branches.includes(c1), "C1 is a branch to T");
		this.assert(t.branches.includes(c2), "C2 is a branch to T");
	}

	test_root {
		this.assertEquals(t.root, p, "P is root to T.");
		this.assertEquals(c1.root, p, "P is root to c1.");
	}

	test_method {
		// Create any old object that has unique methods.
		var o = List();
		p = DhTree(o);
		this.assert(p.prSelfRespondsTo(\do), "Self can search for methods.");
		this.assertEquals(p.prSelfPerform(\class), List, "Self can perform methods.");
		p.prSelfPerform(\addAll, [1, 2]);
		this.assert(p.prSelfPerform(\includes, 1), "Self can perform methods with one arg.");
		p.prSelfPerform(\put, 1, 3);
		this.assertEquals(p.self[1], 3, "Self can perform methods with many args.");
	}

	test_trunksMethod {
		// Create any old object that has unique methods.
		var o = List();
		p.self = o;
		c1.prTrunkPerform(\addAll, [1, 2]);
		this.assert(p.prSelfPerform(\includes, 1), "Trunk methods bubble up to branches, performed on trunk.");
	}

	test_trunkProperty {
		var c;
		p.self = DhAtom[
			\cat -> \meow,
		];
		t.self = DhAtom[
			\dog -> \woof,
		];
		c = c1.trunkProperty(\dog);
		this.assertEquals(c, \woof, "Trunk properties bubble up 1 level.");
		c = c1.trunkProperty(\cat);
		this.assertEquals(c, \meow, "Root properties bubble up 2 levels.");
		c = c1.trunkProperty(\hamster);
		this.assert(c.isNil(), "Empty root properties do not bubble up.");
	}
}
