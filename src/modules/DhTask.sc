DhTask : DhObject {
	var <> notifier;
	var function;

	*new {
		^ super.new.init;
	}

	init {
		super.init;
		function = DhDependencyInjectionContainerObject(nil);
		^ this;
	}

	run {
		this.buildFunction;
		^ function.func.value(this);
	}

	buildFunction {
		if (notifier.isNil.not) {
			function = DhDependencyInjectionContainerObject.fromFunction({
				arg t;
				t.addressMap.find(notifier, t).notify();
			});
			function.isFactory = true;
		};
	}

	/**
	 * Sets our function to call a method on a target.
	 */
	buildMethodFunction {
		function = {
			arg t;

		};
	}

	buildNotifierFunction {

	}

	configure {
		if (config.isNil.not) {

		};
	}
}
