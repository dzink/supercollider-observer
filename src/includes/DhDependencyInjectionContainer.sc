DhDependencyInjectionContainer : DhNillable {

	*new {
		var d = super.new();
		^ d.init;
	}

	init {
		validKeys = IdentityDictionary[];
		^ this;
	}

	put {
		arg key, func ... args;


		super.put(key, func);
		^ this;
	}

	putFunction {
		arg key, func ... args;
		if (func.isKindOf(Function)) {
			func = DhDependencyInjectionContainerObject.fromFunction(func, args);
		};
		super.put(key, func);
		^ this;
	}

	putFactory {
		arg key, func ... args;
		this.put(key, func, *(args));
		func.isFactory = true;
		^ this;
	}

	at {
		arg key;
		var object = this.safeAt(key);
		var value = object;
		if (this.evaluatedAt(key).not) {
			value = object.evaluate();
			if (object.isFactory.not) {
				super.put(key, value);
			};
		};
		^ value;
	}

	safeAt {
		arg key;
		^ super.at(key);
	}

	evaluatedAt {
		arg key;
		var o = this.safeAt(key);
		^ o.isKindOf(DhDependencyInjectionContainerObject).not;
	}

	objects {
		^ this;
	}

}
