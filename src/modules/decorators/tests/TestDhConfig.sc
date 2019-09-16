TestDhConfig : TestDh {
	var c;

	setUp {
		c = DhConfig();
	}

	tearDown {
		c.free;
	}

	test_put {
		c.put("a.b.c", 1);
		this.assertEquals(c.at("a.b.c"), 1, "Puts and gets configs.");
	}

	test_slotSyntax {
		c["a.b.c"] = 1;
		this.assertEquals(c["a.b.c"], 1, "Puts and gets slot configs.");
	}

	test_default {
		var d = DhConfig();
		c["a.b.c"] = 1;
		d["a.d.e"] = 2; // Test value.
		d["a.b.f"] = 3; // Test array.
		d["a.b.c"] = 4; // Test existing value.
		c = c.default(d);
	}

	test_config {
		var s;
		c["a.b.c.d"] = 1;
		c["a.b.c.e.f"] = 2;
		c["a.b.ce.de"] = 4;
		s = c.subConfig("a.b.c");
		this.assertEquals(s.at("d"), 1, "Sub config works with objects");
		this.assertEquals(s.at("e.f"), 2, "Sub config works with long named objects.");
		this.assertEquals(s.at("e.de"), nil, "Sub config doesn't find partial configs.");
	}

	test_dic {
		var value;
		c[\f] = {
			2;
		};
		this.assert(c.isEvaluatedAt(\f).not, "Function is not yet evaluated.");
		value = c[\f];
		this.assertEquals(value, 2, "Function is evaluated after being requested.");
		this.assert(c.isEvaluatedAt(\f), "Function is marked evaluated.");
	}
}
