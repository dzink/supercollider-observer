TestDhNotificationContext : TestDh {
	var a, b;

	setUp {
		a = DhNotificationContext();
		b = DhNotificationContext();
	}

	tearDown {
		a.free;
		b.free;
	}

	test_matchAny {
		a[\cat] = \meow;
		a[\dog] = \woof;
		b[\cat] = \meow;
		this.assert(b.match(a), "Matching any passes positive.");

		b[\cat] = \woof;
		this.assert(b.match(a).not, "Matching any fails negative.");
	}

	test_matchAll {
		b = DhNotificationContextAll();
		a[\cat] = \meow;
		a[\dog] = \woof;
		b[\cat] = \meow;
		b[\dog] = \woof;
		this.assert(b.match(a), "Matching all passes positive.");

		b[\cat] = \woof;
		this.assert(b.match(a).not, "Matching all fails negative.");

		b[\cat] = \meow;
		a[\cat] = nil;
		this.assert(b.match(a).not, "Matching all fails if target is missing.");
	}

	test_matchNone {
		b = DhNotificationContextNone();
		a[\cat] = \meow;
		a[\dog] = \woof;
		b[\cat] = \woof;
		b[\dog] = \meow;
		this.assert(b.match(a), "Matching none passes positive.");

		b[\cat] = \meow;
		this.assert(b.match(a).not, "Matching none fails negative.");

		b[\cat] = \meow;
		a[\cat] = nil;
		this.assert(b.match(a).not, "Matching none passes if target is missing.");
	}
}
