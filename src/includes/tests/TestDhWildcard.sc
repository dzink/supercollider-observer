TestDhWildcard : TestDh {
	test_wildcard {
		var string = "meow";
		this.assert(DhWildcard.wildcardMatch(string, "me*"), "Wildcard matches end");
		this.assert(DhWildcard.wildcardMatch(string, "*eo*"), "Wildcard matches both ends");
		this.assert(DhWildcard.wildcardMatch(string, "*ow"), "Wildcard matches start");
		this.assert(DhWildcard.wildcardMatch(string, "m?ow"), "Wildcard ? matches a single character");
		this.assert(DhWildcard.wildcardMatch(string, "me?ow").not, "Wildcard ? does not match zero characters");
		this.assert(DhWildcard.wildcardMatch(string, "eo").not, "Wildcard does not match without asterisks");
		this.assert(DhWildcard.wildcardMatch(string, "").not, "Wildcard does not match empty string");
	}

	test_wildCards {
		var strings = [
			"meow",
			"mmmeow",
			"hisss",
		];

		this.assertEquals(DhWildcard.wildcardMatchAll(strings, "*").size, 3, "Asterisk matches all");
		this.assertEquals(DhWildcard.wildcardMatchAll(strings, "m*eow").size, 2, "Asterisk matches many or none");
		this.assertEquals(DhWildcard.wildcardMatchAll(strings, "m*eow").size, 2, "Asterisk matches many or none");
	}

	test_regex {
		var string = "meow";
		this.assert(DhWildcard.regexMatch(string, ".e.*"), "Wildcard matches regex");
	}
}
