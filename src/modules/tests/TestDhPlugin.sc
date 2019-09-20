TestDhPlugin : TestDh {
	var p;

	setUp {
		p = DhPlugin();
		p.setId(\p);
	}

	tearDown {
		p.free;
	}

	test_heirarchy {
		var p2 = DhService();
		p.addService(\p2, p2);
		this.assert(p.getBranches.includes(p2), "Parent contains submodule");
		this.assertEquals(p2.getTrunk, p, "Submodules knows its parent.");
	}

	test_method {
		var n;
		p.addMethod(\fifteen, {
			15
		});
		n = p.fifteen();
		this.assertEquals(n, 15, "Method is transparently added to plugin.");

		p.addMethod(\plusTen, {
			arg self, n;
			n + 10
		});

		n = p.plusTen(20);
		this.assertEquals(n, 30, "Method can use arguments.");
	}

	test_data {
		p[\cat] = \meow;
		this.assertEquals(p[\cat], \meow, "Data is stored on plugin.");
	}

	test_serviceDiscovery {
		var p2 = DhPlugin();
		var s = DhService();
		p2.setId(\p2);
		p2.addService(\s, s);
		p.setTrunk(p2);

		this.assertEquals(p.getService(\s), s, "Subplugin finds its parent's service");

		s = DhService();
		p.addService(\s, s);
		this.assertEquals(p.getService(\s), s, "Subplugin finds its own service");

	}

}
