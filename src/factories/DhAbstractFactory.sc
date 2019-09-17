DhAbstractFactory {
	var <>configs;
	var <builtMembers;

	new {
		arg configs;
		^ super.new.init(configs);
	}

	init {
		arg aConfigs;
		configs = aConfigs;
		builtMembers = List();
	}

	build {
		arg config;
		var member;
		member = this.buildClass(config);
		this.buildBasics(member, config);
		this.buildMembers(member, config);
		builtMembers = builtMembers.add(member);
		^ member;
	}

	buildClass {
		arg config;
		var class = config["class"] ?? DhAtom;
		 ^ class.asSymbol.asClass.new();//perform("*new".asSymbol);
	}

	buildBasics {
		arg m, config;
		this.getBasicPropertyList(config).keysValuesDo {
			arg key, propertyConfig;
			var sourceKey = propertyConfig[\sourceKey];
			var method = propertyConfig[\targetMethod];
			if (config.includesKey(sourceKey)) {
				var data = config[sourceKey];
				m.perform(method.asSymbol, data);
			};
		};
	}

	getBasicPropertyList {
		arg config;
		^ this.getPropertyList(config, \basicProperties);
	}

	buildMembers {
		arg m, config;
		this.getMemberPropertyLists(config).keysValuesDo {
			arg key, buildConfig;
			this.buildMemberGroup(m, config, buildConfig);
		};
	}

	buildMemberGroup {
		arg m, config, buildConfig;
		var member;
		var memberList = config[buildConfig[\key]];
		var method = buildConfig[\targetMethod].asSymbol;
		var baseConfig = configs[buildConfig[\base_config]];

		if (memberList.isNil.not) {
			memberList.keysValuesDo {
				arg key, memberConfig;
				var member = this.buildMemberGroupMember(memberConfig, baseConfig);
				m.perform(method, member, key, memberConfig);
			};
		};
	}

	buildMemberGroupMember {
		arg memberConfig, baseConfig;
		var member;
		memberConfig.default(baseConfig);
		member = this.build(memberConfig);
		^ member;
	}

	getMemberPropertyLists {
		arg config;
		^ this.getPropertyList(config, \buildKeys);
	}

	getPropertyList {
		arg config, key;
		var candidates = config[key];
		candidates = candidates.select({
			arg candidate;
			(candidate.includesKey(\active).not or: {candidate[\active].asBoolean} );
		});
		^ candidates;
	}
}
