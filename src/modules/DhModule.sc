DhModule : DhPlugin {
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
