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
		this.buildMemberTypes(member, config);
		builtMembers = builtMembers.add(member);
		^ member;
	}

	buildClass {
		arg config;
		var class = config["class"] ?? DhAtom;
		 ^ class.asSymbol.asClass.new();//perform("*new".asSymbol);
	}

	/**
	 * Assign basic properties, which are defined in the \basicProperties key
	 * of the member config.
	 */
	buildBasics {
		arg m, config;
		var propertyList = this.getBasicPropertyList(config);
		if (propertyList.isNil.not) {
			propertyList.weightedKeysValuesDo {
				arg key, propertyConfig;
				var sourceKey = propertyConfig[\sourceKey];
				var method = propertyConfig[\targetMethod];
				if (config.includesKey(sourceKey)) {
					// var data = config[sourceKey];
					// m.perform(method.asSymbol, data);
				};
			};
		};
	}

	getBasicPropertyList {
		arg config;
		^ this.getPropertyList(config, \basicProperties);
	}

	/**
	 * Goes through the list of member types.
	 * Get lists of member types, then pass configs to build member groups.
	 */
	buildMemberTypes {
		arg m, config;
		var memberPropertyLists = this.getMemberPropertyLists(config);
		if (memberPropertyLists.isNil.not) {
			memberPropertyLists.weightedKeysValuesDo {
				arg key, buildConfig;
				this.buildMemberGroup(m, config, buildConfig);
			};
		};
	}

	/**
	 * Goes through the list of member instances.
	 * Get the list of members, then iterate across its keys. We need to use its
	 * keys because sometimes simple members (such as notifiers) simply need to
	 * have their key defined.
	 */
	buildMemberGroup {
		arg m, config, buildConfig;
		var member;
		var memberList = config[buildConfig[\key]];
		var method = buildConfig[\targetMethod].asSymbol;
		var baseConfig = configs[buildConfig[\base_config]];

		if (memberList.isNil.not) {
			memberList.weightedKeysValuesDo {
				arg key, memberConfig;
				var member;

				// If a member list is just keys, the configs will be empty.
				if (memberConfig.isNil) {
					memberConfig = DhConfig();
				};

				memberConfig.default(DhConfig[\key -> key]);
				member = this.buildMemberGroupMember(memberConfig, baseConfig);
				m.perform(method, key, member, memberConfig);
			};
		};
	}

	/**
	 * Build each member. Add a base config for defaults for each.
	 */
	buildMemberGroupMember {
		arg memberConfig, baseConfig;
		var member;

		memberConfig.default(baseConfig);
		member = this.build(memberConfig);
		member.storeConfig(memberConfig);
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
