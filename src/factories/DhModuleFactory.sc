DhModuleFactory {
	var configs;

	build {
		arg config;
		var m;
		m = config["class"].new();
		this.buildBasics(m, config);
		this.buildServices(m, config);
		this.buildNotifiers(m, config);
		this.buildObservers(m, config);

		^ m;
	}

	buildBasics {
		arg m, config;
		m.id = config["id"];
	}

	buildServices {
		arg m, config;
		var services = config["services"];
		services.keysValuesDo {
			arg key, serviceConfig;
			var service = serviceConfig[\class].new();
		}
	}
}
