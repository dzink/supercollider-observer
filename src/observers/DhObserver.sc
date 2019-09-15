DhObserver {

	var <>function = nil;
	var <>filter = nil;
	var <>weight = 0;
	var <>async = false;
	var <blocked = false;

	*new {
		arg function = {}, filter = nil;
		var observer = super.new();
		^ observer.init(function, filter);
	}

	init {
		arg func, filt;
		function = func;
		^ this;
	}

	/**
	 * Receives a notification and runs the function.
	 */
	respond {
		arg message;
		var result;
		if (filter.isNil or: {filter.match(message.context)}) {
			result = function.value(message);
			^ result;
		};
		^ nil;
	}

	observe {
		arg notifier;
		notifier.addObserver(this);
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
