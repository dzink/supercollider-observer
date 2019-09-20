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

	test_convertToObjects {
		var branches = [p, t, c1, c2].collect({
			arg branch;
			branch.tree;
		});
		this.assert(branches[0].isKindOf(DhTree), "Branch is DhTree object.");
		branches = DhTree.asSelves(branches);
		this.assert(branches[0].isKindOf(DhObject), "Now it's an object.");
		this.assertEquals(branches.size, 4, "4 objects in the array.");
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

		// Get branch whose id is c2.
		select = p.selectBranches({
			arg branch;
			// [\select, branch.id, branch.getSelf.id].postln;
			branch.id == \c2;
		}, inf);
		this.assertEquals(select.size, 1, "Select returns the only matching branch.");
		this.assertEquals(select[0].id, \c2, "Selects the proper grandchild branch.");

		// Get branches whose id's begin with c.
		select = p.selectBranches({
			arg branch;
			branch.id.asString[0].asSymbol == \c;
		}, inf).collect({
			arg object;
			object.id;
		});
		this.assertEquals(select.size, 2, "Select returns both only matching branches.");
		this.assert(select.includes(\c1), "Adds c1 to the list.");
		this.assert(select.includes(\c2), "Adds c2 to the list also.");

		// Get no brnaches because stop after first level.
		select = p.selectBranches({
			arg branch;
			\end;
		}, inf);
		this.assertEquals(select.size, 0, "Select stopped at proper depth.");
	}

	test_getTrunks {
		var trunks = c2.getTrunks();
		// this.assert(trunks.includes(\p), "Child's trunks includes parent \\t.");
		// this.assert(trunks.includes(\p), "Child's trunks includes grandparent \\p.");
	}

	test_selectTrunks {
		// var select;
		//
		// // Get trunk whose id is p.
		// select = c1.selectTrunks({
		// 	arg branch;
		// 	branch.id == \p;
		// }, inf);
		// this.assertEquals(select.size, 1, "Select returns the only matching trunk.");
		// this.assertEquals(select[0].id, \p, "Selects the proper grandchild branch.");

		// // Stop after first level.
		// select = c1.selectTrunks({
		// 	arg branch;
		// 	branch.id == \p;
		// }, inf);
		// this.assertEquals(select.size, 0, "Select stops after the first level.");
	}

}
