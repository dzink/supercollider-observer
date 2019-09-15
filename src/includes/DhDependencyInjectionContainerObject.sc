DhDependencyInjectionContainerObject {
	var <func;
	var <args;
	var <>isFactory = false;

	*fromFunction{
		arg func, args;
		var d = super.new();
		^ d.init(func, args);
	}

	init {
		arg aFunc, aArgs;
		func = aFunc;
		args = aArgs;
		^ this;
	}

	evaluate {
		arg container;
		var v = func.value(container, *(args));
		^ v;
	}
}
