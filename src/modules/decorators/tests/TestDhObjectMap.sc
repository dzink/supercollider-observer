TestDhObjectMap : TestDh {
	var o, l1, l2, l3, l4;

	setUp {
		l1 = DhModule().setId(\l1);
		l2 = DhObject().setId(\l2).setTrunk(l1);
		l3 = DhObject().setId(\l3).setTrunk(l2);
		l4 = DhObject().setId(\l4).setTrunk(l2);
		[\out].postln;
		o = DhObjectMap().setId(\map);
		l1.addService(\map, o);
	}

	tearDown {
		o.free;
		l1.free;
		l2.free;
		l3.free;
		l4.free;
	}

	test_address {
		var map = l4.getService(\map);
		this.assertEquals(l4.address, "l1/l2/l4", "Address is generated correctly");
		this.assertEquals(map.matchRoute("l1/l2/l4"), l4, "Address is followed correctly");
		this.assertEquals(map.matchRoute("l2/l4", l2), l4, "Sub-Address is followed correctly");
		// map.drawTree;
	}

	test_idemp {
		var map = l4.getService(\map);
		this.assertEquals(l4, map.matchRoute(l4.address), "Idempotency check.")
	}

	test_shortcut {
		// var map = l4.getService(\map);
		this.assertEquals(l4, l4.router.matchRoute(l4.address), "Router shortcut check.")
	}


}
