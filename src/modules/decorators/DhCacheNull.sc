DhCacheNull : DhCache {
	var <validKeys;

	*new {
		var c = super.new();
		^ c.init;
	}

	init {
		^ this;
	}

	cacheAside {
		arg key, default = {};
		^ default.value();
	}
}
