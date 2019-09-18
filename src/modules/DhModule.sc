DhModule : DhPlugin {
	var <id;
	var < tree;
	var < submodules;

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
		submodules = DhDependencyInjectionContainer();
		super.init();
		^ this;
	}

	addSubmodule {
		arg module, key, memberConfig;
		var moduleDic = {
			// module.run(\initModule);
			module;
		};
		this.submodules[key]= moduleDic;
		^ this;
	}

	// service {
	// 	arg key;
	// 	if (this.services[key].isNil.not) {
	// 		^ this.services[key];
	// 	};
	// 	this.trunkFilter({
	// 		arg m;
	// 		if (m.services.services[key].isNil.not) {
	// 			^ m.services.services[key];
	// 		};
	// 	});
	// 	^ nil;
	// }
	//
	// registerNotifier {
	// 	arg key, notifier;
	// 	// Add overwrite warning.
	// 	if (notifier.isNil) {
	// 		notifier = DhNotifier();
	// 	};
	// 	notifiers[key] = notifier;
	// }
	//
	// notifier {
	// 	arg key;
	// 	^ notifiers[key];
	// }
	//
	// notify {
	// 	arg key, message = nil, context = nil;
	// 	if (notifiers.isNil) {
	// 		notifiers = DhAtom();
	// 	};
	// 	context = this.buildContext(context);
	// 	notifiers[key].notify(message, context);
	// 	^ this;
	// }
	//
	// registerObserver {
	// 	arg key, observer;
	// 	// Add overwrite warning.
	// 	if (observer.isNil) {
	// 		observer = DhObserver();
	// 	};
	// 	observers[key] = observer;
	// }
	//
	// observer {
	// 	arg key;
	// 	^ observers[key];
	// }
	//
	// observe {
	// 	arg key, notifier;
	// 	observers[key].observe(notifier);
	// 	^ this;
	// }
	//
	// buildNotification {
	// 	arg message = nil, context = nil;
	// 	var m;
	// 	context = context.clone()
	// 	 // = DhNotification()
	// }
	//
	// buildContext {
	// 	arg context;
	// 	if (context.isNil) {
	// 		context = DhContext();
	// 	};
	// 	context.importDefaults(DhContext[
	// 		\moduleId -> id,
	// 	]);
	//
	// 	^ context;
	// }
	//
	// log {
	// 	arg message, messageType = \log;
	// 	this.logger.log(message, messageType);
	// 	^ this;
	// }
	//
	// debug {
	// 	arg message, level = 1;
	// 	this.log(message, \debug);
	// }
}
