

DhObserver {

	var <>function = nil;
	var filter = nil;
	var <>weight = 0;

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
		arg notification;
		var result;
		// if (filter.isNil or: filter.matches(notification)) {
			result = function.value(notification);
			^ result;
		// };
		// ^ nil;
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
