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
		^ this.function.func.value(this);
	}

	function {
		^ function ?? {};
	}

	assignFunction {
		arg otherFunction = {};
		function = DhDependencyInjectionContainerObject.fromFunction(otherFunction);
		^ this;
	}

	/**
	 * Sets our function to call a method on a target.
	 */
	assignMethodFunction {
		arg targetId = "", method = '';
		^ this.assignFunction({
			arg t;
			var target = t.addressMap.find(targetId, t);
			if (target.isKindOf(DhObject)) {
				target.perform(method);
			} {
				("Target is not a kind of target, id: " ++ targetId).error;
			};
		});
	}

	assignNotifierFunction {
		arg notifierId = "", method = \notify, message = nil;
		^ this.assignFunction({
			arg t;
			var notifier = t.addressMap.find(notifierId, t);
			if (notifier.isKindOf(DhNotifier)) {
				// [\obscount, notifier.observers.size].postln;
				notifier.perform(method.asSymbol, message);
			} {
				(this.getAddress ++ ": Notifier is not a kind of notifier, id: " ++ notifierId).error;
			};
		});
	}

	configure {
		arg config;
		if (config.isNil.not) {
			switch (config[\taskType].asSymbol,
				\method, {
					this.assignMethodFunction(config[\target], config[\method].asSymbol);
					[\methh, this.getAddress].postln;
				},
				\notifier, {
					this.assignNotifierFunction(config[\notifierId], config[\notifierMethod], config[\message]);
					[\nottii, this.getAddress].postln;
					// [config].postcs;
				}
			);
			weight = config[\weight] ?? 0;
		};
	}
}
