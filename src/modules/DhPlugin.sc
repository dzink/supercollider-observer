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
		var serviceDic = {
			service.setOwner(this);
			// service.run(\serviceInit);
			service;
		};
		this.services[key]= serviceDic;
		^ this;
	}

	addObserver {
		arg observer, key, memberConfig;
		var observerDic = {
			observer.setOwner(this);
			// observer.run(\observerInit);
			observer;
		};
		this.observers[key]= observerDic;
		^ this;
	}

	addNotifier {
		arg notifier, key, memberConfig;
		var notifierDic = {
			notifier.setOwner(this);
			// notifier.run(\notifierInit);
			notifier;
		};
		this.notifiers[key]= notifierDic;
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
		// @TODO this should look up the tree as well.
		^ services[key];
	}
}
