DhDependencyInjectionContainerObject {
	var < func;
	var <> isFactory = false;
	var extend;

	*fromFunction{
		arg func;
		var d = super.new();
		^ d.init(func);
	}

	init {
		arg aFunc;
		func = aFunc;
		^ this;
	}

	evaluate {
		arg container, args = nil;
		if (args.isNil) {
			^ func.value(container);
		} {
			^ func.value(container, args);
		};
	}

	evaluateAFunc {
		arg container, args = nil;
		if (args.isNil) {
			^ func.value(container);
		} {
			^ func.value(container, args);
		};
	}

	extend {
		arg newFunction;
		var lastFunc = func;
		func = {
			arg container, args;
			var v = if (args.isNil) {
				newFunction.value(lastFunc.value(container), container);
			} {
				newFunction.value(lastFunc.value(container, args), container, args);
			};
			v;
		};
		^ this;
	}
}
