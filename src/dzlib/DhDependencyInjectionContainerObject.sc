DhDependencyInjectionContainerObject {
	var < func;
	var <> isFactory = false;
	var extend;
	var < value;
	var < evaluated = false;

	*fromFunction{
		arg func;
		var d = super.new();
		^ d.init(func);
	}

	init {
		arg aFunc = nil;
		func = aFunc;
		^ this;
	}

	evaluate {
		arg container, args = nil;
		value = if (args.isNil) {
			func.value(container);
		} {
			func.value(container, args);
		};
		evaluated = true;
		^ value;
	}

	evaluateAFunc {
		arg container, args = nil;
		if (args.isNil) {
			^ func.value(container);
		} {
			^ func.value(container, args);
		};
	}

	/**
	 * Take the existing function, and wrap it in another function to extend
	 * its behavior.
	 */
	extend {
		arg newFunction;
		var lastFunc = func;

		if (func.isNil) {
			func = newFunction;
		} {

			// wrap the old function in the new one. Args needs to stay unset if that
			// is how it came.
			func = {
				arg container, args;
				var v = if (args.isNil) {
					newFunction.value(lastFunc.value(container), container);
				} {
					newFunction.value(lastFunc.value(container, args), container, args);
				};
				v;
			};
		};
		^ this;
	}
}
