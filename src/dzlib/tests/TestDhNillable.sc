TestDhNillable : TestDh {
	var n;

	setUp {
		n = DhNillable();
	}

	tearDown {
		n.free;
	}

	test_nillable {
		n[\a] = true;
		this.assert(n[\a], "Object exists.");
		this.assert(n.includesKey(\a), "Object is included.");
		n[\a] = nil;
		this.assert(n[\a].isNil, "Nilled object is still nil but...");
		this.assert(n.includesKey(\a), "Nilled object is included.");
		n.removeAt(\a);
		this.assert(n.includesKey(\a).not, "Removed object is not included.");
		n[\b] = nil;
		this.assert(n[\a].isNil, "Always-nil object can be nil.");
		this.assert(n.includesKey(\b), "Always-nil object is included.");
	}

	test_keys {
		n[\a] = nil;
		this.assert(n.keys.includes(\a), "Keys includes nil object.");
	}

	test_sort {
		var list;
		n[\hamster] = DhAtom[
			\key -> \hamster,
			\weight -> 4,
		];
		n[\cat] = DhAtom[
			\key -> \cat,
			\weight -> -1,
		];
		n[\dog] = DhAtom[
			\key -> \dog,
			\weight -> 1,
		];
		n[\nil] = nil;
		list = n.sortByProperty();
		this.assertEquals(list[0][\key], \cat, "Cat goes first.");
		this.assert(list[1].isNil, "Nil has a weight of 0.");
		this.assertEquals(list[2][\key], \dog, "Dog goes third.");
		this.assertEquals(list[3][\key], \hamster, "Hamster goes fourth.");
	}

	test_sortKey {
		var list;
		n[\hamster] = DhAtom[
			\key -> \hamster,
			\weight -> 4,
		];
		n[\cat] = DhAtom[
			\key -> \cat,
			\weight -> -1,
		];
		n[\dog] = DhAtom[
			\key -> \dog,
			\weight -> 1,
		];
		n[\nil] = nil;
		list = n.sortKeysByProperty();
		this.assertEquals(list[0], \cat, "Cat goes first.");
		this.assertEquals(list[1], \nil, "Nil has a weight of 0.");
		this.assertEquals(list[2], \dog, "Dog goes third.");
		this.assertEquals(list[3], \hamster, "Hamster goes last.");
	}

	test_weightedKeysValuesDo {
		var list = List[];
		n[\hamster] = DhAtom[
			\key -> \hamster,
			\weight -> 4,
		];
		n[\cat] = DhAtom[
			\key -> \cat,
			\weight -> -1,
		];
		n[\dog] = DhAtom[
			\key -> \dog,
			\weight -> 1,
		];
		n[\nil] = nil;
		n.weightedKeysValuesDo {
			arg key, value;
			list.add(value);
		};
		this.assertEquals(list[0][\key], \cat, "Cat goes first.");
		this.assertEquals(list[1], nil, "Nil has a weight of 0.");
		this.assertEquals(list[2][\key], \dog, "Dog goes third.");
		this.assertEquals(list[3][\key], \hamster, "Hamster goes last.");
	}
}
