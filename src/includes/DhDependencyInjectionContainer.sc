DhDependencyInjectionContainer[] {
	var <objects;

	*new {
		var d = super.new();
		^ d.init;
	}

	init {
		objects = DhAtom[];
		^ this;
	}

	put {
		arg key, func ... args;
		if (func.isKindOf(Function)) {
			func = DhDependencyInjectionContainerObject.fromFunction(func, args);
		};
		objects[key] = func;
		^ this;
	}

	putFactory {
		arg key, func ... args;
		if (func.isKindOf(Function)) {
			func = DhDependencyInjectionContainerObject.fromFunction(func, args);
			func.isFactory = true;
		};
		objects[key] = func;
		^ this;
	}

	at {
		arg key;
		var object = objects.at(key);
		^ this.prEvaluate(key, object);
	}

	evaluate {
		arg key;
		this.at(key);
		^ this;
	}

	prEvaluate {
		arg key, object;
		var value;
		value = if (this.prIsUnevaluated(object)) {
			value = object.evaluate(this);
			if (object.isFactory.not) {
				objects[key] = value;
			};
			^ value;
		} {
			^ object;
		};
	}


	prIsUnevaluated {
		arg object;
		^ (object.isKindOf(DhDependencyInjectionContainerObject));
	}

	isEvaluatedAt {
		arg key;
		var o = objects[key];
		^ this.prIsUnevaluated(o).not;
	}

	// Pass thru methods. These allow the DIC to be used like a DhAtom.

	key {
		^ objects.keys;
	}

	post {
		^ objects.post;
	}

	postln {
		^ objects.postln;
	}

	// keysValuesDo {
	// 	arg function;
	// 	^ objects.keysValuesDo(function);
	// }

	doesNotUnderstand {
		arg method ... args;
		if (objects.respondsTo(method)) {
			^ objects.perform(method, *(args));
		};
		^ DoesNotUnderstandError(this, method, args).reportError();
	}
	//
	// >> {
	// 	arg that;
	// 	^ objects.>>(that);
	// }
}
