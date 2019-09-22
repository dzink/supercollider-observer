/**
	DhScheduler
	Works to ensure that asynchronus methods do not interfere with each other,
	and manages the dependencies between them.
	@TODO should this be split into multiple classes?
*/

DhScheduler {
	var condition;
	var semaphore;
	var < maxThreads = 1;
	var waitConditions;
	var waitCount = 0;
	var < activeThreads = 0;
	var < queuedThreads = 0;
	var < timesFired = 0;

	*new {
		^ super.new.init()
	}

	init {
		arg aMaxThreads;
		waitConditions = List();
		activeThreads = 0;
		timesFired = 0;
		semaphore = Condition({this.shouldRun});
		maxThreads = (aMaxThreads ?? 1).asInteger;
		^ this;
	}

	/**
	 * Set the maximum number of concurrent threads.
	 */
	maxThreads_ {
		arg t;
		maxThreads = t;

		// @TODO If this changes, may need to block/unblock a few times.
		^ this;
	}

	isBlocked {
		^ (queuedThreads > maxThreads);
	}

	shouldRun {
		^ (queuedThreads <= maxThreads);
	}

	/**
	 * If more than maxThreads threads are running, pause execution here.
	 */
	block {
		timesFired = timesFired + 1;
		activeThreads = activeThreads + 1;
		queuedThreads = queuedThreads + 1;
		// semaphore.wait();
		this.waitUntil({this.shouldRun})
		^ this;
	}

	/**
	 * Lets the scheduler know that this thread no longer needs to be blocked.
	 */
	unblock {
		activeThreads = activeThreads - 1;
		queuedThreads = queuedThreads - 1;
		semaphore.signal();
		^ this;
	}

	/**
	 * Register a thread, but explicitly do not block.
	 */
	registerThread {
		timesFired = timesFired + 1;
		activeThreads = activeThreads + 1;
		^ this;
	}

	deregisterThread {
		activeThreads = activeThreads - 1;
		^ this;
	}

	/**
	 * Block based on a function until it evaluates to true.
	 */
	waitUntil {
		arg func, interval = 0.2;

		// [\testing, func.value()].postln;
		while ({func.value() != true}) {
			// [\waiting, func.value()].postln;
			interval.wait;
		};
		// [\complete, func.value()].postln;

		^ this;
	}

	/**
	 * Wait for all threads to resolve.
	 */
	waitForAll {
		arg interval = 0.2;
		^ this.waitUntil({ activeThreads <= 0 }, interval);
	}
	/**
	 * Wait for all threads to resolve.
	 */
	waitForQueued {
		arg interval = 0.2;
		^ this.waitUntil({ queuedThreads <= 0 }, interval);
	}

}
