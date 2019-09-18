DhPlugin[] : DhObject {
	var < methods;
	var < data;
	// var < observers;
	// var < notifiers;
	var < services;
	var <> id;

	*new {
		^ super.new.init();
	}

	init {
		methods = DhAtom();
		data = DhDependencyInjectionContainer();
		config = DhConfig();
		services = DhDependencyInjectionContainer();
		// [\servicesInit, services].postln;
		^ this;
	}

	addService {
		arg service, key, memberConfig;
		service.setOwner(this);
		// [\addingService, services.class, this, key].postln;
		services[key] = service;
		^ this;
	}

	addObserver {
		arg observer, key, memberConfig;
		observer.setOwner(this);
		this.observers[key] = observer;
		^ this;
	}

	addNotifier {
		arg notifier, key, memberConfig;
		notifier.setOwner(this);
		this.notifiers[key] = notifier;
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

	observers {
		^ this.service(\observers);
	}

	notifiers {
		^ this.service(\notifiers);
	}

	service {
		arg key;
		[\lookingForService, key, services.keys].postln;
		^ services[key];
	}
}
