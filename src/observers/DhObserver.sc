

DhObserver {

	var <>function = nil;
	var filter = nil;

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

	subscribeTo {
		arg notifier;
		notifier.addObserver(this);
		^ this;
	}
}
