DhLogger : DhService {
	var <> logVerbosity = 10;
	var <> logWarnings = true;
	var <> logErrors = true;

	log {
		arg message, level = 1;
		if (level <= logVerbosity) {
			("Log " ++ logVerbosity ++ ": " ++ message).postln;

		};
		^ this;
	}

	warn {
		arg message;
		if (logWarnings) {
			("Warning: " ++ message).warn;
		};
		^ this;
	}

	error {
		arg message;
		if (logErrors) {
			("Error: " ++ message).error;
		};
		^ this;
	}
}
