DhService : DhAtom {
	var module;

	*new {
		arg module;
		var s = super.new();
		^ s.init(module);
	}

	init {
		arg aModule;
		module = aModule;
		^ this;
	}
}
