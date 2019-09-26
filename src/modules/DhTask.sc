DhTask : DhObject {
	var <> notifier;
	var function;

	*new {
		^ super.new.init;
	}

	init {
		super.init;
		function = DhDependencyInjectionContainerObject(nil);
		\makingTask.postln;
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
				[\func, t.getAddress, t.addressMap.find(notifier, t), t.addressMap.find(notifier, t).id].postln;
				t.addressMap.find(notifier, t).notify();
			});
			function.isFactory = true;
		};
	}

	configure {
		// [\configging, this.id, config, this.config.class].postln;
		// if (config.includesKey(\notifier)) {
		// 	\sadf;
		// };
	}
}
