TestDhApp : TestDhModule {
	var a;

	setUp {
		a = DhApp().setId('app');
	}

	tearDown {
		a.free
	}

	test_addTask {
		var animal = \cat;
		var t = DhTask().setId('task1');
		var t2 = DhTask().setId('task2');
		var n = DhNotifier().setId('notifier1').setTrunk(a);
		var n2 = DhNotifier().setId('notifier2').setTrunk(a);
		var o = DhObserver().setId('observer1').setTrunk(a);
		var o2 = DhObserver().setId('observer2').setTrunk(a);

		a.addMethod(\marmoset, {
			animal = \marmoset;
		});

		a.addTask(t.id, t);

		this.assert(a.tasks.includes(t), "Task is added to the app.");

		o.observe(n);
		o.function = {
			animal = \dog;
		};
		t.assignNotifierFunction(n.getAddress);
		this.assertEquals(animal, \cat, "Observer has not been notified yet.");

		a.run();
		this.assertEquals(animal, \dog, "Observer has been notified.");

		o2.observe(n2);
		o2.function = {
			animal = \seal;
		};
		a.addTask(t2.id, t2);
		t2.assignNotifierFunction(n2.getAddress);

		t.weight = 10;
		t2.weight = 20;
		a.run();
		this.assertEquals(animal, \seal, "Heavier observer has been notified last.");

		t.weight = 30;
		t2.weight = 20;
		a.run();
		this.assertEquals(animal, \dog, "The new heavier observer has been notified last.");

		t.assignMethodFunction(a.getAddress, \marmoset);
		a.run();
		this.assertEquals(animal, \marmoset, "A method task function works.");

		t.assignFunction({
			animal = \chinchilla;
		});
		a.run();
		this.assertEquals(animal, \chinchilla, "A custom task function works.");
	}
}
