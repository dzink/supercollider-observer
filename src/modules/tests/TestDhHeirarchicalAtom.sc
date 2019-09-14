// TestDhHeirarchicalAtom : TestDh {
// 	var p, t, c1, c2;
//
// 	setUp {
// 		p = DhHeirarchicalAtom[
// 			\id -> \p,
// 		];
// 		t = DhHeirarchicalAtom[
// 			\id -> \t,
// 		];
// 		c1 = DhHeirarchicalAtom[
// 			\id -> \c1,
// 		];
// 		c2 = DhHeirarchicalAtom[
// 			\id -> \c2,
// 		];
// 		p.branch(t);
// 		t.branch(c1, c2);
// 	}
//
// 	tearDown {
// 		p.free;
// 		t.free;
// 		c1.free;
// 		c2.free;
// 	}
//
// 	// test_dummy {
// 	// 	this.assert(true);
// 	// 	[p, t, c1, c2].collect {
// 	// 		arg b;
// 	// 		b.trunk.postln;
// 	// 	};
// 	// }
//
// 	test_trunk {
// 		this.assertEquals(t.trunk, p, "P is trunk to T.");
// 		this.assertEquals(c1.trunk, t, "T is trunk to c1.");
// 		this.assertEquals(c2.trunk, t, "T is trunk to c2.");
// 	}
//
// 	test_branches {
// 		this.assert(p.branches.includes(t), "T is a branch to P");
// 		this.assert(t.branches.includes(c1), "C1 is a branch to T");
// 		this.assert(t.branches.includes(c2), "C2 is a branch to T");
// 	}
//
// 	test_root {
// 		this.assertEquals(t.root, p, "P is root to T.");
// 		this.assertEquals(c1.root, p, "P is root to c1.");
// 	}
//
// 	test_trunksMethod {
// 		var h;
// 		p.tree.self = "abcdefg";
// 		h = c1.prTrunkPerform(\find, "de");
// 		this.assertEquals(h, 3, "Root methods bubble up to the branch.");
// 	}
//
// 	test_trunkProperty {
// 		var c;
// 		p.cat = \meow;
// 		t.dog = \woof;
// 		c = c1.trunkProperty(\dog);
// 		this.assertEquals(c, \woof, "Trunk properties bubble up 1 level.");
// 		c = c1.trunkProperty(\cat);
// 		this.assertEquals(c, \meow, "Root properties bubble up 2 levels.");
// 		c = c1.trunkProperty(\hamster);
// 		this.assert(c.isNil(), "Empty root properties do not bubble up.");
// 	}
// }
