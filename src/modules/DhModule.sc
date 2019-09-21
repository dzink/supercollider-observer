DhModule : DhPlugin {
	var < submodules;

	*new {
		var m = super.new();
		m = m.init();
		^ m;
	}

	init {
		super.init();
		submodules = DhDependencyInjectionContainer();
		^ this;
	}

	addSubmodule {
		arg key, module, memberConfig;
		var self = this;
		var moduleDic = {
			// module.run(\initModule);
			module;
		};
		this.submodules[key]= module;
		this.addBranches(module);
		^ this;
	}



}
