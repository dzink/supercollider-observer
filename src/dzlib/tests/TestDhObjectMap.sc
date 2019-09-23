TestDhObjectMap : TestDh {
	var o, l1, l2, l3, l4;

	setUp {
		l1 = DhModule().setId(\l1);
		l2 = DhObject().setId(\l2).setTrunk(l1);
		l3 = DhObject().setId(\l3).setTrunk(l2);
		l4 = DhObject().setId(\l4).setTrunk(l2);
	}

	tearDown {
		o.free;
		l1.free;
		l2.free;
		l3.free;
		l4.free;
	}

	test_route {
		// var map = l4.getService(\map);
		this.assertEquals(l4.getRoute(), 'l1/l2/l4', "Route is generated correctly");
		this.assert(l4.getRouteMap().isNil.not, "Route Map is generated.");
		this.assertEquals(l2.getRouteMap(), l1.getRouteMap(), "Route Map is shared between objects.");
		this.assert(l2.getRouteMap().list.includes(\l1), "Route Map stores routes.");
		this.assertEquals(l2.findByRoute(\l1), l1, "And the routes correctly point to the root member.");
		this.assertEquals(l2.findByRoute('l1/l2/l4'), l4, "And the routes correctly point to a sub member.");
		this.assertEquals(l2.findByRoute("l1/l2/l4"), l4, "Strings work also.");
		this.assertEquals(l2.findByRoute(l4.getRoute()), l4, "Idempotency check.");
		this.assertEquals(l4.findByRouteFrom("l4", l2), l4, "Router shortcut check.");

		// Check this last
		l4.free;
		this.assertEquals(l2.findByRoute('l1/l2/l4'), nil, "Freed objects are no longer in the map.");
	}
}
