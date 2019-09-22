TestDhScheduler : TestDh {

	test_block {
		var s = DhScheduler();
		var n = true;
		s.block();
		fork {
			s.block();
			n = false;
			s.unblock();
		};
		this.assert(n, "N is initially true");
		this.wait({s.isBlocked()}, "Scheduler gets blocked.", 1);
		s.unblock();
		this.wait({n.not}, "Scheduler is eventually unblocked by the fork.", 1);
	}

	test_blockAndContinue {
		var s = DhScheduler();
		s.block();
		this.assert(true, "Scheduler kept rolling.");
		s.unblock();
	}

	test_blockMany {
		var s = DhScheduler();
		var n = false;
		s.maxThreads = 3;
		s.block();
		s.block();
		s.block();
		this.assert(true, "Can run up to three threads");
		fork {
			s.block();
			this.assert(true, "Eventually the fork is unblocked");
			n = true;
			s.unblock();
		};
		this.wait({s.isBlocked()}, "The next block stops the scheduler", 1);
		s.unblock();
		this.wait({n}, "Scheduler is eventually unblocked by the fork.", 1);
	}

	test_waitUntil {
		var s = DhScheduler();
		var n = false;
		var m = true;
		fork {
			0.1.wait;
			this.assert(m, "Scheduler is waiting for n to be true.");
			n = true;
		};
		s.waitUntil({n});
		m = false;
		this.assert(n, "Scheduler waited for n to be true.");
	}

	test_waitUntilAlreadyTrue {
		var s = DhScheduler();
		var n = true;
		var m = true;
		fork {
			s.waitUntil({n}, 10); // Long test interval.
			this.assert(n, "Scheduler didn't wait");
			0.1.wait;
			n = false;
			m = false;
		};
		this.assert(m, "Initially m was true.");
		this.wait({m.not}, "But then m was quickly made false.", 0.2);
	}

	test_waitForAll {
		var s = DhScheduler();
		var n = false;
		s.maxThreads = inf;
		4.do {
			fork {
				s.registerThread();
				0.1.wait;
				this.assert(n.not, "Waiting for all threads to complete.");
				s.deregisterThread();
			};
		};
		this.wait({s.activeThreads == 4}, "All 4 threads are running", 1);
		s.waitForAll();
		this.assertEquals(s.activeThreads, 0, "All 4 threads are complete");
		n = true;
		this.assert(true, "Eventually we are complete.");
	}
}
