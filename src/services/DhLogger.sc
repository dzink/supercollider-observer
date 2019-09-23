DhLogger : DhService {
	var <> logVerbosity = 10;
	var <> logWarnings = true;
	var <> logErrors = true;

	log {
		arg message, level = 1;
		if (level <= logVerbosity) {
			message.postln;
		};
		^ this;
	}

	warn {
		arg message;
		if (logWarnings) {
			message.warn;
		};
		^ this;
	}

	error {
		arg message;
		if (logErrors) {
			message.error;
		};
		^ this;
	}
}
