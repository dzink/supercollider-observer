DhObject {
	var config;
	var owner;
	var id;

	storeConfig {
		arg aConfig;
		config = aConfig;
		^ this;
	}

	setOwner {
		arg anOwner;
		owner = anOwner;
		^ this;
	}
}
