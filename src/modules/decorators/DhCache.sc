DhCache : DhNillable {

	cacheAside {
		arg key, default = {};
		if (this.includesKey(key).not) {
			var v = default.value();
			this.put(key, v);
			^ v;
		};
		^ this[key];
	}

}
