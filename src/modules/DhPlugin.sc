DhPlugin[] {
	var < methods;
	var < data;
	var < config;

	*new {
		^ super.new.init();
	}

	init {
		methods = DhAtom();
		data = DhDependencyInjectionContainer();
		config = DhConfig();
		^ this;
	}

	at {
		arg key;
		^ data[key];
	}

	put {
		arg value, key;
		^ data.put(value, key);
	}

	addMethod {
		arg key, func;
		if (func.isClosed.not) {
			"Function is not closed and cannot be compiled safely.".warn;
		};
		methods[key] = func;
	}

	doesNotUnderstand {
		arg method ... args;
		if (methods.includesKey(method)) {
			(this.class.asString ++ " does not respond to " ++ method ++ " but it has that method in its methods").warn;
			^ methods[method].value(this, *(args));
		};
		if (data.respondsTo(method)) {
			^ data.perform(method, *(args));
		};
		// @TODO there should be a better error here.
		(this.class.asString ++ " does not respond to " ++ method).error;
	}

	defaultDataStore {
		^ data;
	}
}
