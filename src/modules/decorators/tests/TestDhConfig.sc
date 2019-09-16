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
		this.assertEquals(c[\a][\b][\c], 1, "Puts and gets configs.");
		this.assertEquals(c.at("a.b.c"), 1, "Configs accessible by key.");
	}

	test_putOverwrite {
		c.put("a.b.c", 1);
		c.put("a.b", 1);
		this.assertEquals(c[\a][\b], 1, "A Scalar overwrites DhConfig.");
		this.assert(c.includesKey("a.b.c").not, "Configs no longer have sub keys.");
		c.put("a.b.c", 1);
		this.assertEquals(c["a.b.c"], 1, "A lower scalar replaces overwrites DhConfig.");
		this.assert(c["a.b"].isKindOf(DhConfig), "Now b is a config container.");
	}

	test_slotSyntax {
		c["a.b.c"] = 1;
		this.assertEquals(c["a.b.c"], 1, "Puts and gets slot configs.");
	}

	test_keys {
		var keys;
		c["a.b.c"] = 1;

		keys = c.fullKeys;
		this.assert(keys.includes("a".asSymbol), "Gets root key.");
		this.assert(keys.includes("a.b".asSymbol), "Gets 2nd key.");
		this.assert(keys.includes("a.b.c".asSymbol), "Gets data key.");

		keys = c.keys;
		this.assert(keys.includes("a".asSymbol).not, "Does not list root key.");
		this.assert(keys.includes("a.b".asSymbol).not, "Does not list 2nd key.");
		this.assert(keys.includes("a.b.c".asSymbol), "Gets data key.");

		this.assert(c.includesKey("a.b.d").not, "False positive attempt fails.");
		this.assert(c.includesKey("a.b.c.d").not, "Deep false positive attempt fails.");
		c.put("a.b", 1);
		this.assert(c.includesKey("a.b.c").not, "Old keys are gone.");
	}

	test_default {
		var d = DhConfig();
		c["a.b.c"] = 1;
		c["a.b.nil"] = nil;
		d["a.d.e"] = 2; // Test value.
		d["a.b.f"] = 3; // Test array.
		d["a.b.c"] = 4; // Test existing value.
		// c["a.b.nil"] = 2;
		c = c.default(d);
		this.assertEquals(c["a.b.c"], 1, "Originals not overwritten with defaults.");
		// this.assertEquals(c["a.d.e"], 2, "Defaults adds new objects.");
		// this.assert(c["a.b.nil"].isNil(), "Config is nillable.");
		// this.assert(c.includesKey("a.b.nil"), "Config is nillable.");

		d["a.b.c.d"] = 5;
		c = c.default(d);
		// c.asCompileString.postln;
	}

	test_dic {
		var value;
		c[\f] = {
			2;
		};
		this.assert(c.evaluatedAt(\f).not, "Function is not yet evaluated.");
		value = c[\f];
		this.assertEquals(value, 2, "Function is evaluated after being requested.");
		this.assert(c.evaluatedAt(\f), "Function is marked evaluated.");
	}
	//
	// test_yaml {
	// 	var yaml = "'duck':
  // 'duck':
  //   'goose':
  //   - 1
  //   - true
  //   - FALSE
  //   - nil
  //   - null
  //   'moose':
  //     'what?'";
	// 	c = DhConfig.fromYaml(yaml);
	// 	c.keys.postln;
	// 	this.assertEquals(c["duck.duck.moose"], "what?", "Config yaml import imports strings (easy).");
	// 	this.assert(["duck.duck.goose"].isKindOf(Array), "Config yaml import works with arrays.");
	// 	this.assertEquals(c["duck.duck.goose.0"].class, Float, "Config yaml import turns numberlike strings into Floats.");
	// 	this.assertFloatEquals(c["duck.duck.goose.0"], 1, "Config yaml import imports floats.");
	// 	this.assertEquals(c["duck.duck.goose.1"], true, "Config yaml import imports true.");
	// 	this.assertEquals(c["duck.duck.goose.2"], false, "Config yaml import imports false.");
	// 	this.assertEquals(c["duck.duck.goose.3"], nil, "Config yaml import imports nil.");
	// 	this.assertEquals(c["duck.duck.goose.4"], nil, "Config yaml import imports NULL.");
	// }
	//
	test_nil {
		c["a.b.c"] = 1;
		c["a.b.c"] = nil;
		[\nillist, c.keys].postln;
		this.assert(c.includesKey("a.b.c"), "Config can be nilled without losing its key.");
	}
}
