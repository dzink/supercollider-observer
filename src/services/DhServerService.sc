DhServerService : DhService {
	var options;
	var server;

	getOptions {
		if (options.isNil) {
			options = ServerOptions();
		};
		^ options;
	}

	setOption {
		arg param, value;
		var o = this.getOptions();
		var method = (param.asString ++ '_').asSymbol;
		if (o.hasMethod(method)) {
			o.perform(method, value);
		};
		this.error("No ServerOptions method: " ++ param);
		^ this;
	}

	getServer {
		if (server.isNil) {
			server = Server();
		};
		^ server;
	}

	attachOptionsToServer {
		this.getServer().options = this.getOptions();
	}
}
