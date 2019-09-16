TestDhConfig : TestDh {
	var c;

	setUp {
		c = DhConfig();
	}

	tearDown {
		c.free;
	}

	// test_put {
	// 	c.put("a.b.c", 1);
	// 	this.assertEquals(c[\a][\b][\c], 1, "Puts and gets configs.");
	// 	this.assertEquals(c.at("a.b.c"), 1, "Configs accessible by key.");
	// }
	//
	// test_slotSyntax {
	// 	c["a.b.c"] = 1;
	// 	this.assertEquals(c["a.b.c"], 1, "Puts and gets slot configs.");
	// }
	//
	// test_keys {
	// 	var keys;
	// 	c["a.b.c"] = 1;
	// 	keys = c.keys.sort;
	// 	this.assertEquals(keys[0], "a", "Gets root key.");
	// 	this.assertEquals(keys[1], "a.b", "Gets 2nd key.");
	// 	this.assertEquals(keys[2], "a.b.c", "Gets data key.");
	// }
	//
	// test_default {
	// 	var d = DhConfig();
	// 	c["a.b.c"] = 1;
	// 	c["a.b.nil"] = nil;
	// 	d["a.d.e"] = 2; // Test value.
	// 	d["a.b.f"] = 3; // Test array.
	// 	d["a.b.c"] = 4; // Test existing value.
	// 	c["a.b.nil"] = 2;
	// 	c = c.default(d);
	// 	this.assertEquals(c["a.b.c"], 1, "Originals not overwritten with defaults.");
	// 	this.assertEquals(c["a.d.e"], 2, "Defaults adds new objects.");
	// 	this.assert(c["a.b.nil"].isNil(), "Config is nillable.");
	//
	// 	c.asCompileString.postln;
	// }
	//
	// test_dic {
	// 	var value;
	// 	c[\f] = {
	// 		2;
	// 	};
	// 	this.assert(c.isEvaluatedAt(\f).not, "Function is not yet evaluated.");
	// 	value = c[\f];
	// 	this.assertEquals(value, 2, "Function is evaluated after being requested.");
	// 	this.assert(c.isEvaluatedAt(\f), "Function is marked evaluated.");
	// }
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

	// test_nil {
	// 	c["a.b.c"] = 1;
	// 	c["a.b.c"] = nil;
	// 	this.assert(c.includesKey("a.b.c"), "Config can be nilled without losing its key.");
	// }
}
