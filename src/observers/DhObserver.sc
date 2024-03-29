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

	< {
		arg comp;
		if (comp.isKindOf(DhObserver)) {
			comp = comp.weight;
		};
		^ this.weight < comp;
  }

}
