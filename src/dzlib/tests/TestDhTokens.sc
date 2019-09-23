TestDhTokens : TestDh {
	var t;

	setUp {
		t = DhTokens[
			\root_dir -> ".",
			\name -> "Daniel",
			\catNoise -> "meow",
		];
	}

	tearDown {
		t.free;
	}

	test_tokenList {

	}

	test_tokenize {
		var s = "{root_dir}/users/{name}/{catNoise}}.sc";
		this.assertEquals(t.tokenize(s), "./users/Daniel/meow}.sc", "Tokenizer replaces tokens.");
		this.assertEquals(t.tokenize(s, [\catNoise]), "{root_dir}/users/{name}/meow}.sc", "Tokenizer replaces a subset of tokens.");
		s = "{name}/{name}";
		this.assertEquals(t.tokenize(s), "Daniel/Daniel", "Tokenizer works on multiple instances of the same token.")

	}
}
