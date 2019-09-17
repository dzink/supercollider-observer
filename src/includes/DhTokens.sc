DhTokens : DhAtom {

	tokenize {
		arg string, keys = nil;

		// Get matches, then filter out the bracketed ones bc sc can't do subs.
		var matches = string.findRegexp("\{(\\w*)\}").collect({
			arg match;
			match[1];
		}).as(Set).select({
			arg match;
			"\{.*\}".matchRegexp(match).not;
		}).collect({
			arg match;
			match.asSymbol;
		}).asArray();

		// Intersect with the given keys.
		matches = matches.sect(keys ?? this.keys);

		matches.do {
			arg match;
			var replacement = this[match];
			if (replacement.isNil.not) {
				string = string.replace("{" ++ match ++ "}", replacement);
			};
		}
		^ string;
	}

}
