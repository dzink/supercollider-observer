TestDhDependencyInjectorSubset : TestDh {
	var d;
	var s;

	setUp {
		d = DhDependencyInjectionContainer();
		s = DhDependencyInjectorSubset(d, 'a');
	}

	tearDown {
		d.free;
		s.free
	}

	set_at {
		var v;
		s[\b] = { \meow };
		this.assert(s.convertKey(\b), 'a/b', "Subset converts key correctly.");
		this.assert(s.includesKey('b'), "Subset contains an object.");
		this.assert(d.includesKey('a/b'), "Parent DIC contains an object.");
		this.assertEquals(s.safeAt('b'), d.safeAt('a/b'), "The subset contains the same object as the DIC.");
		this.assert(s.evaluatedAt(\b).not, "Subset is not evaluated yet.");
		this.assert(d.evaluatedAt('a/b').not, "DIC is not evaluated yet.");

		v = s[\b];
		this.assert(s.evaluatedAt(\b).not, "Subset is now evaluated.");
		this.assert(d.evaluatedAt('a/b').not, "DIC is now evaluated.");
		this.assertEquals(v, \meow, "Subset is evaluated correctly.");
		this.assertEquals(d['a/b'], \meow, "DIC is evaluated correctly.");
	}

}
