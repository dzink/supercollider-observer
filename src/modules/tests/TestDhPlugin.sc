TestDhPlugin : TestDh {
	var p;

	setUp {
		p = DhPlugin();
	}

	tearDown {
		p.free;
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

}
