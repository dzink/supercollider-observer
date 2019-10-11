DhObserver : DhObject {

	var < function = nil;
	var <> filter = nil;
	var <> weight = 0;
	var <> async = false;
	var < maxThreads = nil;

	*new {
		arg function = {}, filter = nil;
		var observer = super.new();
		^ observer.init(function, filter);
	}

	init {
		arg func, filt;
		super.init();
		function = func;
		^ this;
	}

	/**
	 * Receives a notification and runs the function.
	 */
	respond {
		arg message, scheduler;
		var result = nil;
		if (filter.isNil or: {filter.match(message.context)}) {
			result = function.value(message);
		};
		^ result;
	}

	observe {
		arg notifier;
		notifier.addObserver(this);
		^ this;
	}

	function_ {
		arg aFunction;
		if (aFunction.isKindOf(Function).not) {
			aFunction = { aFunction };
		};
		function = aFunction;
		^ this;
	}

	configure {
		arg config;
		if (config.isNil.not) {
			var self = this;
			var method = config[\method];
			var targetId = config[\targetId];
			var notifierId = config[\notifierId];
			var target = self.find(targetId, self);

			if (method.isNil.not) {
				function = {
					arg message, scheduler;
					[\observer, message].postln;
					target.perform(method.asSymbol, message);
				};
			};
			if (notifierId.isNil.not) {
				var notifier = self.find(notifierId, self);
				this.observe(notifier);
			};

		};
	}

	< {
		arg comp;
		if (comp.isKindOf(DhObserver)) {
			comp = comp.weight;
		};
		^ this.weight < comp;
  }

}
