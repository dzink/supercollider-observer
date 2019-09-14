TestDhObservers : TestDh {
	var n, n2, o, o2, do;

	setUp {
		n = DhNotifier();
		n2 = DhNotifier();
		o = DhObserver();
		o2 = DhObserver();
		do = DhNotifierObserver();
	}

	tearDown {
		n.free;
		n2.free;
		o.free;
		o2.free;
	}

	test_notify {
		var animal = \cat;
		o.function = {
			animal = \dog;
		};
		o.observe(n);
		n.notify;
		this.assertEquals(animal, \dog, "Observer responds to empty nofification");
	}

	test_emptyObserverFunction {
		o.observe(n);
		n.notify;
		this.assert(true, "Empty observer function does not crash");
	}

	test_weightedObserver {
		var animals = List[];
		o.function = {
			animals.add(\dog);
		};
		o2.function = {
			animals.add(\cat);
		};
		o.observe(n);
		o2.observe(n);

		o.weight = -10;
		o2.weight = 10;
		n.observers.sort;
		n.notify;
		this.assertEquals(animals[0], \dog, "Observer initially sorts dog first");
		this.assertEquals(animals[1], \cat, "Observer initially sorts cat second");

		// Reverse the weights, re-sort.
		o.weight = 10;
		o2.weight = -10;
		n.observers.sort;
		n.notify;
		this.assertEquals(animals[2], \cat, "Observer after resort sorts cat first");
		this.assertEquals(animals[3], \dog, "Observer after resort sorts dog second");
	}

	test_notifierObserver {
		var animals = List[];
		o.function = {
			animals.add(\lion);
		};
		o2.function = {
			animals.add(\tiger);
		};
		do.addObserver(o);
		do.addObserver(o2);
		do.observe(n);
		do.observe(n2);

		// Testing the first notifier.
		n.notify();
		this.assert(animals.includesAll(List[\lion, \tiger]), "Duplex responds to n");

		// Clearing the list and testing the first notifier.
		n2.notify();
		this.assert(animals.includesAll(List[\lion, \tiger]), "Duplex responds to n2");
	}

	test_notification {
		var m = DhNotification("meow");
		var result;
		o.observe(n);
		o.function = {
			arg message;
			result = message.message;
		};
		n.notify(m);
		this.assertEquals(result, "meow", "Observer passes message.");
	}


	test_notificationFilter {
		var nf = DhContext[
			\cat -> \meow,
		];
		var of = DhContext[
			\cat -> \meow,
		];
		var m = DhNotification("meow", nf);
		var result = nil;
		o.observe(n);
		o.function = {
			arg message;
			result = message.message;
		};
		o.filter = of;
		n.notify(m);
		this.assertEquals(result, "meow", "Observer passes message thru filter.");
	}

	test_notificationFilterFail {
		var nf = DhContext[
			\cat -> \meow,
		];
		var of = DhContext[
			\cat -> \woof,
		];
		var m = DhNotification("meow", nf);
		var result = nil;
		o.observe(n);
		o.function = {
			arg message;
			result = message.message;
		};
		o.filter = of;
		n.notify(m);
		this.assertEquals(result, nil, "Observer filters unmatching message.");
	}

}
