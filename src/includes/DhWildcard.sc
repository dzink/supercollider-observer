DhWildcard {

	/**
	 * Determines if a single string matches a wildcard.
	 * Wildcards can include * (0 or more) or + (1 or more).
	 */
	*wildcardMatch {
		arg string, wildcard, escaped = false;

		if (escaped.not) {
			wildcard = DhWildcard.wildcardToRegex(wildcard, escaped);
		};
		^ wildcard.matchRegexp(string.asString);
	}

	*regexMatch {
		arg string, regex;
		^ regex.matchRegexp(string.asString);
	}

	/**
	 * Performs a wildcard filter on a collection. Only matching strings are
	 * returned.
	 * Wildcards can include * (0 or more) or + (1 or more).
	 */
	*wildcardMatchAll {
		arg collection, wildcard, escaped = false;
		if (escaped.not) {
			wildcard = DhWildcard.wildcardToRegex(wildcard);
		};
		^ DhWildcard.regexMatchAll(collection, wildcard);
	}

	*regexMatchAll {
		arg collection, regex;
		^ collection.select({
			arg string;
			regex.matchRegexp(string.asString);
		});
	}

	/**
	 * Performs a wildcard filter on a collection. Returns true if any members
	 * match.
	 * Wildcards can include * (0 or more characters) or ? (1 character).
	 */
	*wildcardMatchAny {
		arg collection, wildcard, escaped = false;
		if (escaped.not) {
			wildcard = DhWildcard.wildcardToRegex(wildcard, escaped);
		};
		^ DhWildcard.regexMatchAny(collection, wildcard);
	}

	*regexMatchAny {
		arg collection, regex;
		collection.do({
			arg string;
			if (regex.matchRegexp(string.asString)) {
				^ true;
			};
		});
		^ false;
	}

	*prRegexEscapees {
		^ ["\\", "\/", "[", "]", "{", "}", ".", "(", ")", "^", "&", "+", "?", "|", "*"];
	}

	*prRegexEscapeesWhitelist {
		arg whitelist = List[];
		var escapees = DhWildcard.prRegexEscapees();
		^ escapees.select({
			arg escapee;
			whitelist.includes(escapee);
		});
	}

	*prRegexEscapeesBlacklist {
		arg blacklist = List[];
		var escapees = DhWildcard.prRegexEscapees();
		^ escapees.reject({
			arg escapee;
			blacklist.includes(escapee);
		});
	}

	*wildcardToRegex {
		arg wildcard, escaped = false;
		wildcard = wildcard.asString();
		if (escaped.not) {
			var escapees = DhWildcard.prRegexEscapeesBlacklist(["*", "?"]);
			DhWildcard.prRegexEscape(wildcard, escapees);
		};
		wildcard = wildcard.replace("*", ".*");
		wildcard = wildcard.replace("?", ".");
		wildcard = "^" ++ wildcard ++ "$";
		^ wildcard;
	}

	*prRegexEscape {
		arg string, escapees = nil;
		escapees = escapees ?? { DhWildcard.prRegexEscapees };
		escapees.do {
			arg escapee;
			string = string.replace(escapee, "\\" ++ escapee);
		};
		^ string;
	}






}
