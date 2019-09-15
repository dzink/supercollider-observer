/*
 * DhNotifier.sc
 * A class to send notifications to observeers.
 */
DhNotifier {
	var id = 0;
	var <observers = nil;
	var <>contextDefaults = nil;

	*new {
		arg contextDefaults = nil;
		var n = super.new();
		^ n.init;
	}

	init {

	}

	/**
	 * Basic waterfall notifications. Observers run in weight order.
	 */
	notify {
		arg message;
		observers.do {
			arg observer;
			var resolver = DhAsyncResolver();
			var increment = observer.async.not().asInteger();
			resolver.increment(increment);
			fork {
				observer.respond(message);
				resolver.decrement(increment);
			};
			resolver.wait;
		};
		^ this;
	}

	/**
	 * Observe all notifications async. Obviously, observer weight does nothing.
	 */
	notifyAsync {
		arg message;
		var c = 0;
		var resolver = DhAsyncResolver();
		observers.do {
			arg observer;
			var increment;

			// Don't increment/decrement on async observers.
			increment = observer.async.not().asInteger();
			resolver.increment(increment);
			fork {
				observer.respond(message);
				resolver.decrement(increment);
			};
		};
		^ resolver;
	}

	/**
	 * Observe all notifications async, but wait for them to resolve before
	 * continuing. Observer weight does nothing.
	 */
	notifyResolve {
		arg message;
		var resolver = this.notifyAsync(message);
		resolver.wait();
		^ resolver;
	}

	addObserver {
		arg observer;
		if (observers.isKindOf(List).not) {
			observers = SortedList[];
		};
		observers.add(observer);
		^ this;
	}

}
