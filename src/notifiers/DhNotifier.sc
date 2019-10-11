/*
 * DhNotifier.sc
 * A class to send notifications to observeers.
 */
DhNotifier : DhObject {
	var id = 0;
	var < observers = nil;
	var <> contextDefaults = nil;
	var cache;

	*new {
		arg contextDefaults = nil;
		var n = super.new();
		^ n.init;
	}

	init {
		super.init();
		observers = SortedList[];
		cache = DhCache();
		^ this;
	}

	/**
	 * Basic waterfall notifications. Observers run in weight order.
	 */
	notify {
		arg message;
		var results = DhResponse();
		var scheduler = DhScheduler();
		results.scheduler = scheduler;
		observers.sort.do {
			arg observer;
			var result;
			scheduler.block();
			result = observer.respond(message, scheduler);
			results[observer.getAddress()] = result;
			scheduler.unblock();
		};
		^ results;
	}

	notifyAsync {
		arg message;
		var results = DhResponse();
		var scheduler = DhScheduler();
		results.scheduler = scheduler;
		observers.sort.do {
			arg observer;
			var result;
			scheduler.registerThread();
			fork {
				result = observer.respond(message, scheduler);
				results[observer.getAddress()] = result;
				scheduler.deregisterThread();
			}
		};
		^ results;
	}

	notifyResolve {
		arg message;
		^ this.notifyAsync(message).resolve();
	}

	addObserver {
		arg observer;
		observers.add(observer);
		^ this;
	}


}
