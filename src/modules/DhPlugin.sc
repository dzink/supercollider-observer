DhPlugin : DhObject {
	var < data;
	var services;
	var < cache;

	*new {
		^ super.new.init();
	}

	init {
		super.init();
		methods = DhAtom();
		data = DhDependencyInjectionContainer();
		config = DhConfig();
		services = DhDependencyInjectionContainer();
		^ this;
	}

	addService {
		arg key, service, memberConfig;
		var serviceDic = {
			service.serviceInit(service);
			service;
		};
		services.putFunction(key, serviceDic);
		this.addBranches(service);

		// Cache service is special, as it needs to be referenced directly to clear
		// the cache's service location list.
		if (key == \cache) {
			cache = service;
		};
		cache.clear('service.*');
		^ this;
	}

	addObserver {
		arg key, observer, memberConfig;
		var observerDic = {
			// observer.run(\observerInit);
			observer;
		};
		// this.addBranches(observer);
		this.observers.putFunction(key, observerDic);
		^ this;
	}

	addNotifier {
		arg key, notifier, memberConfig;
		var notifierDic = {
			// notifier.run(\notifierInit);
			notifier;
		};
		this.notifiers.putFunction(key, notifierDic);
		^ this;
	}

	addMethod {
		arg key, func, config;
		if (func.isClosed.not) {
			"Function is not closed and cannot be compiled safely.".warn;
		};
		methods[key] = func;
	}

	addMethods {
		arg methods;
		methods.keysValuesDo {
			arg key, func;
			if (func.isKindOf(Function)) {
				this.addMethod(key, func, methods);
			} {
				("AddMethod: " ++ func.asString ++ " is not a function").error;
			};
		};
	}

	at {
		arg key;
		^ data[key];
	}

	put {
		arg value, key;
		^ data.put(value, key);
	}

	doesNotUnderstand {
		arg method ... args;
		if (methods.includesKey(method)) {
			// (this.class.asString ++ " " ++ this.address ++ " does not respond to " ++ method ++ " but it has that method in its methods").warn;
			^ methods[method].value(this, *(args));
		};
		if (data.respondsTo(method)) {
			^ data.perform(method, *(args));
		};
		// @TODO there should be a better error here.
		// (this.class.asString ++ " " ++ this.address ++ " does not respond to " ++ method).error;
	}

	defaultDataStore {
		^ data;
	}

	observers {
		^ this.getService(\observers);
	}

	notifiers {
		^ this.getService(\notifiers);
	}

	getService {
		arg key;
		if (this.hasService(key)) {
			^ services[key];
		};
		^ this.prInheritService(key);
	}

	hasService {
		arg key;
		^ services.includesKey(key);
	}

	configure {
		super.configure();
		^ this;
	}

}
