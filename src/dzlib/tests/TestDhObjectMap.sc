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

	test_address {
		// var map = l4.getService(\map);
		l4.addressMap.list.postln;
		this.assertEquals(l4.getAddress(), 'l1/l2/l4', "Address is generated correctly");
		this.assert(l4.addressMap().isNil.not, "Address Map is generated.");
		this.assertEquals(l2.addressMap(), l1.addressMap(), "Address Map is shared between objects.");
		this.assert(l2.addressMap().list.includes(\l1), "Address Map stores addresses.");
		this.assertEquals(l2.addressMap.find(\l1), l1, "And the addresses correctly point to the root member.");
		this.assertEquals(l2.addressMap.find('l1/l2/l4'), l4, "And the addresses correctly point to a sub member.");
		this.assertEquals(l2.addressMap.find("l1/l2/l4"), l4, "Strings work also.");
		this.assertEquals(l2.addressMap.find(l4.getAddress()), l4, "Idempotency check.");
		this.assertEquals(l4.addressMap.find("./l4", l2), l4, "Address shortcut check.");

		// Check this last
		l4.free;
		this.assertEquals(l2.addressMap.find('l1/l2/l4'), nil, "Freed objects are no longer in the map.");
	}
}
