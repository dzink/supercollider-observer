TestDhModule : TestDh {
	var m, p;

	setUp {

		m = DhModule(\m);
		p = DhModule(\p);
	}

	tearDown {
		m.free;
		p.free;
	}

	test_heirarchy {
		p.addSubmodule(\m, m);

		// We have to touch the submodule from its parent to have it init itself.
		p.submodules[\m];
		this.assert(p.getBranches.includes(m), "Parent contains submodule");
		this.assertEquals(m.getTrunk, p, "Submodules knows its parent.");
	}

	// test_notifiers {
	// 	var o;
	// 	var animal = \bison;
	// 	p.registerNotifier(\ping);
	// 	o = DhObserver({
	// 		animal = \leopard;
	// 	});
	// 	o.observe(p.notifiers[\ping]);
	// 	m.registerObserver(\pong, o);
	// 	p.notifiers[\ping].notify();
	// 	this.assertEquals(animal, \leopard, "Modules can notify/observe each other.");
	// }
	//
	// test_trunk {
	// 	var prop;
	// 	p[\cat] = \meow;
	// 	// this.assertEquals(m.trunk[\cat], \meow, "Modules can access root props.")
	// }
	//
	// test_trunksProperties {
	// 	var prop;
	// 	p[\cat] = \meow;
	// 	// prop = m.trunksProperty(\cat);
	// 	// this.assertEquals(prop, \meow, "Modules can access trunk props.")
	// }

}
