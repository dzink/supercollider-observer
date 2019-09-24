TestDhNotificationService : TestDh {
	var s;

	setUp {
		s = DhNotificationService().setId('notifiers');
	}

	tearDown {
		s.free;
	}

	test_notifiers {
		var animal = \cat;
		var p = DhPlugin().setId(\p);
		var n1 = DhNotifier().setId(\n1);
		var n2 = DhNotifierObserver().setId(\n2);
		var o = DhObserver().setId(\o);
		var n3;

		s.setTrunk(p);
		// s.registerNotifier(\n1, n1);
		// s.registerNotifier(\n2, n2);
		p.addService(\notifiers, s);
		p.addNotifier(\n1, n1);
		s.addNotifier(\n2, n2);
		p.addObserver(\o, o);

		this.assertEquals(p.addressMap.find('p/notifiers/n1'), s[\n1], "N1 is added to the addressr and service.");
		this.assertEquals(p.addressMap.find('p/notifiers/n2'), s[\n2], "N2 is added to the addressr and service.");

		s.notifiers.postcs;

		o.function = {
			animal = \dog;
		};
		o.addressMap.list.postcs;
		o.observe(o.addressMap.find('p/notifiers/n1'));

		this.assertEquals(animal, \cat, "Animal starts as a cat.");
		animal = \cat;
		n1.notify();
		this.assertEquals(animal, \dog, "The notifier runs the observer.");

		o.observe(o.addressMap.find('p/notifiers/n2'));
		animal = \cat;
		s.notify(\n2);
		this.assertEquals(animal, \dog, "The notifier service runs the observer.");

		n3 = DhNotifier().setId(\n3).setTrunk(p);
		s[\n2].observe(s.addressMap.find('p/n3'));
		animal = \cat;
		n3.notify();
		this.assertEquals(animal, \dog, "The notifier service passes through to observers.");
	}

}
