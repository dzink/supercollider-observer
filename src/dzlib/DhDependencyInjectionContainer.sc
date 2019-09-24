/**
 * Caches either objects, or the instructions on how to create them.
 * This is helpful for making sure that not
 */
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
		arg key, func;
		super.put(key, func);
		^ this;
	}

	/**
	 * Put a function into the DIC.
	 */
	putFunction {
		arg key, func;
		if (func.isKindOf(Function)) {
			func = DhDependencyInjectionContainerObject.fromFunction(func);
		};
		super.put(key, func);
		^ this;
	}

	/**
	 * Put a function into the DIC, but mark it so that it is reevaluated each
	 * time.
	 */
	putFactory {
		arg key, func;
		this.put(key, func);
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

	/**
	 * Returns the object without evaluating it.
	 */
	protectAt {
		arg key;
		^ super.at(key);
	}

	evaluatedAt {
		arg key;
		var o = this.safeAt(key);
		^ o.isKindOf(DhDependencyInjectionContainerObject).not;
	}

	extend {
	 arg key, function;
	 this.safeAt(key).extend(function);
	}

	objects {
		^ this;
	}

}
