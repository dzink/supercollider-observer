TestDhAtom : TestDh {
	var a;

	setUp {
		a = DhAtom();
	}

	tearDown {
		a.free;
	}

	test_at {
		a.cat = \meow;
		this.assertEquals(a.cat, \meow, "Property-style assignment.");
		this.assertEquals(a[\cat], \meow, "Key-style assignment.");
	}

	test_sort {
		var list;
		a.hamster = DhAtom[
			\key -> \hamster,
		];
		a.cat = DhAtom[
			\key -> \cat,
			\weight -> -1,
		];
		a.dog = DhAtom[
			\key -> \dog,
			\weight -> 1,
		];
		list = a.sortByProperty();
		this.assertEquals(list[0].key, \cat, "Cat goes first.");
		this.assertEquals(list[1].key, \hamster, "Hamster goes second.");
		this.assertEquals(list[2].key, \dog, "Dog goes third.");
	}

	test_string {
		a["dog"] = \woof;
		this.assertEquals(a[\dog], \woof, "String keys are converted to symbols on put.");
		this.assertEquals(a["dog"], \woof, "String keys are converted to symbols on get.");
	}
}
