TestDhDependencyInjectionContainerObject : TestDh {
	var o;
	var c;
	var n = true;

	setUp {
		c = DhDependencyInjectionContainer();
		o = DhDependencyInjectionContainerObject.fromFunction({
			arg c, args = 0;
			n = false;
			5 + args;
		});
	}

	tearDown {
		o.free;
		c.free;
	}

	test_evaluate {
		var result;
		n = true;
		result = o.evaluate(c);
		this.assert(n.not, "Function was evaluated.");
		this.assertEquals(result, 5, "Function returned proper value.");

		result = o.evaluate(c, [1, 2, 3]);
		this.assertEquals(result.size, 3, "Function with args returns array when added to 5.");
		this.assert(result.includes(8), "Function with args includes the new value.");

		o.extend({
			arg r, c, args;
			n = \meow;
			r + 7;
		});
		result = o.evaluate(c);
		this.assertEquals(n, \meow, "Extended function was evaluated.");
		this.assertEquals(result, 12, "Extended function returned proper value.");

		result = o.evaluate(c, [1, 2, 3]);
		this.assertEquals(result.size, 3, "Extended function with args returns array when added to 5.");
		this.assert(result.includes(15), "Extended function with args includes the new value.");

	}

	test_extend {
		var result;
		n = true;

	}
}
