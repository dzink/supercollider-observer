TestDhTree : TestDh {
	var p, t, c1, c2;

	setUp {
		p = DhObject();
		t = DhObject();
		c1 = DhObject();
		c2 = DhObject();
		p.setId(\p);
		t.setId(\t);
		c1.setId(\c1);
		c2.setId(\c2);
		t.setTrunk(p);
		t.addBranches(c1, c2);
	}

	tearDown {
		p.free;
		t.free;
		c1.free;
		c2.free;
	}

	test_trunk {
		this.assertEquals(t.getTrunk, p, "P is trunk to T.");
		this.assertEquals(c1.getTrunk, t, "T is trunk to c1.");
		this.assertEquals(c2.getTrunk, t, "T is trunk to c2.");
	}

	test_branches {
		this.assert(p.getBranches.includes(t), "T is a branch to P");
		this.assert(t.getBranches.includes(c1), "C1 is a branch to T");
		this.assert(t.getBranches.includes(c2), "C2 is a branch to T");
	}

	test_selectBranches {
		var select;
		select = p.selectBranches

	}
	//
	// test_root {
	// 	this.assertEquals(t.root, p, "P is root to T.");
	// 	this.assertEquals(c1.root, p, "P is root to c1.");
	// }
	//
	// test_method {
	// 	// Create any old object that has unique methods.
	// 	var o = List();
	// 	p = DhTree(o);
	// 	this.assert(p.prSelfRespondsTo(\do), "Self can search for methods.");
	// 	this.assertEquals(p.prSelfPerform(\class), List, "Self can perform methods.");
	// 	p.prSelfPerform(\addAll, [1, 2]);
	// 	this.assert(p.prSelfPerform(\includes, 1), "Self can perform methods with one arg.");
	// 	p.prSelfPerform(\put, 1, 3);
	// 	this.assertEquals(p.self[1], 3, "Self can perform methods with many args.");
	// }
	//
	// test_trunksMethod {
	// 	// Create any old object that has unique methods.
	// 	var o = List();
	// 	p.self = o;
	// 	c1.trunkPerform(\addAll, [1, 2]);
	// 	this.assert(p.prSelfPerform(\includes, 1), "Trunk methods bubble up to branches, performed on trunk.");
	// }
	//
	// test_trunkProperty {
	// 	var c;
	// 	p.self = DhAtom[
	// 		\cat -> \meow,
	// 	];
	// 	t.self = DhAtom[
	// 		\dog -> \woof,
	// 	];
	// 	c = c1.trunkProperty(\dog);
	// 	this.assertEquals(c, \woof, "Trunk properties bubble up 1 level.");
	// 	c = c1.trunkProperty(\cat);
	// 	this.assertEquals(c, \meow, "Root properties bubble up 2 levels.");
	// 	c = c1.trunkProperty(\hamster);
	// 	this.assert(c.isNil(), "Empty root properties do not bubble up.");
	// }
	//
	// test_trunkFilter {
	// 	var c;
	// 	p.self = DhAtom[
	// 		\cat -> \meow,
	// 	];
	// 	t.self = DhAtom[
	// 		\dog -> \woof,
	// 	];
	// 	c = c1.prTrunkFilter({
	// 		arg o;
	// 		o[\dog] == \woof;
	// 	});
	// 	this.assertEquals(c, t, "Trunk filter finds immediate parent.");
	// 	c = c1.prTrunkFilter({
	// 		arg o;
	// 		o[\cat] == \meow;
	// 	});
	// 	this.assertEquals(c, p, "Trunk filter finds root.");
	// }
}
