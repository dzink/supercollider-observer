TestDhDependencyInjectionContainer : TestDh {
	var d;
	var b = false;

	setUp {
		d = DhDependencyInjectionContainer();
		b = false;
		d[\f] = {
			b = true;
			2;
		};
	}

	tearDown {
		d.free;
	}

	test_at {
		var value;
		this.assert(d.isEvaluatedAt(\f).not, "Function is not yet evaluated.");
		value = d[\f];
		this.assertEquals(value, 2, "Function is evaluated after being requested.");
		this.assert(d.isEvaluatedAt(\f), "Function is marked evaluated.");

		// Use the b switch to see if the function is run a second time.
		this.assert(b, "B was switched to true.");
		b = false;
		value = d[\f];
		this.assert(b.not, "Function was not run a second time.");
	}
}
