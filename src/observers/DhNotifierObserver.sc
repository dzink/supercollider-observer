/**
 * This allows for a generalized many to many connections between notifiers and
 * observers. Any time any notifier sends a message to this observer, it passes
 * the message along to all its observers.
 */
DhNotifierObserver : DhNotifier {
	var <notifier = nil;
	var <observer = nil;

	*new {
		var dnotifier = super.new();
		^ dnotifier.init();
	}

	init {
		super.init();
		notifier = DhNotifier();
		observer = DhObserver({
			arg message;
			this.respond(message);
		});
		^ this;
	}

	observe {
		arg notifier;
		observer.observe(notifier);
		^ this;
	}

	respond {
		arg message;
		this.notify(message);
	}

	filter_ {
		arg f;
		this.observer.filter = f;
	}
}
