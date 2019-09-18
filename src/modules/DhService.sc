/**
 * A service in Dh context is a manager of functions and data, that is
 * accessible from anywhere in the system.
 * Potential uses of a service: data store, Supercollider server, bus manager,
 * cache, etc.
 * Modules can access services by traversing down their tree trunks until they
 * find a matching service. This way, a module can override a global service
 * with a special one just for its children.
 * For example, if a submodule has two children, a server and a synth, it can
 * override the global server with one of its own, which the synth would then
 * access when it called for its server service.
 * Many services have members - for example, a bus manager will have named
 * buses which can be stored. Members of a service should be
 * inserted as functions which resolve to an object; once they are accessed,
 * they are run, and the result is then stored for further access.
 */
DhService : DhPlugin {
	var module;
	var functions;

	*new {
		var s = super.new();
		^ s.init();
	}

	init {
		// members = DhDependencyInjectionContainer();
		super.init();
		^ this;
	}

	registerFunction {
		arg key, func = {};
		// @TODO should this be only open functions?
		functions[key] = func;
		^ this;
	}

	/**
	 * Run a method on the service.
	 */
	run {
		arg key ... args;
		^ functions[key].value(module, this, *(args));
	}

	// registerMember {
	// 	arg key, func;
	//
	// }
	//
	// at {
	// 	arg key;
	// 	^ members[key];
	// }
	//
	// put {
	// 	arg key, value;
	// 	members[key] = value;
	// 	^ this;
	// }

}
