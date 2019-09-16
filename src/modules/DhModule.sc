DhModule : DhAtom {
	var <id;
	var <notifiers;
	var <observers;
	var serviceManager;
	var tree;
	var functions;
	var <services;

	*new {
		arg id;
		var m = super.new();
		m = m.init(id);
		^ m;
	}

	init {
		arg i;
		id = i;
		tree = DhTree(this);
		services = DhAtom();
		functions = DhAtom();
		notifiers = DhAtom();
		observers = DhAtom();
		^ this;
	}

	service {
		arg key;
		if (this.services[key].isNil.not) {
			^ this.services[key];
		};
		this.trunkFilter({
			arg m;
			if (m.services.services[key].isNil.not) {
				^ m.services.services[key];
			};
		});
		^ nil;
	}

	registerNotifier {
		arg key, notifier;
		// Add overwrite warning.
		if (notifier.isNil) {
			notifier = DhNotifier();
		};
		notifiers[key] = notifier;
	}

	notifier {
		arg key;
		^ notifiers[key];
	}

	notify {
		arg key, message = nil, context = nil;
		if (notifiers.isNil) {
			notifiers = DhAtom();
		};
		context = this.buildContext(context);
		notifiers[key].notify(message, context);
		^ this;
	}

	registerObserver {
		arg key, observer;
		// Add overwrite warning.
		if (observer.isNil) {
			observer = DhObserver();
		};
		observers[key] = observer;
	}

	observer {
		arg key;
		^ observers[key];
	}

	observe {
		arg key, notifier;
		observers[key].observe(notifier);
		^ this;
	}

	buildNotification {
		arg message = nil, context = nil;
		var m;
		context = context.clone()
		 // = DhNotification()
	}

	buildContext {
		arg context;
		if (context.isNil) {
			context = DhContext();
		};
		context.importDefaults(DhContext[
			\moduleId -> id,
		]);

		^ context;
	}
}
