DhAbstractFactory {
	var <> configs;
	var < builtMembers;
	var functionCache;
	var dic;
	var addressMap;

	*new {
		arg configs;
		var f = super.new();
		^ f.init(configs);
	}

	init {
		arg aConfigs;
		configs = aConfigs;
		builtMembers = List();
		functionCache = DhCache();
		addressMap = DhObjectMap();
		dic = DhDependencyInjectionContainer();
		^ this;
	}

	setAddressMap {
		arg anotherAddressMap;
		addressMap = anotherAddressMap;
		^ this;
	}

	setDic {
		arg anotherDic;
		dic = anotherDic;
		^ this;
	}
	buildKindOf {
		arg key;
		var config = configs[key];
		^ this.buildFromConfig(config);
	}

	buildFromConfig {
		arg config, parent = nil;
		var member;

		member = this.buildClass(config);
		member.setDic(dic).setAddressMap(addressMap);
		this.buildBasics(member, config);
		this.placeMember(member, config, parent);
		builtMembers = builtMembers.add(member);
		member.setConfig(config);
		this.buildBranchTypes(member, config);
		^ member;
	}

	buildClass {
		arg config;
		var class = config["class"] ?? DhAtom;
		 ^ class.asSymbol.asClass.new();//perform("*new".asSymbol);
	}

	placeMember {
		arg member, config, parent = nil;

		if (config[\id].asString.beginsWith("core.")) {
			member.setId(config[\key]);
		} {
			member.setId(config[\id]);
		};
		if (parent.isNil.not) {
			var method = config[\addMethod].asSymbol;
			// member.setTrunk(parent);
			parent.perform(method, config[\key], member, config);
		};
		^ this;
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
				var method = propertyConfig[\addMethod];
				if (config.includesKey(sourceKey)) {
					var data = config[sourceKey];
					m.perform(method.asSymbol, data);
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
	buildBranchTypes {
		arg m, config;
		var memberPropertyLists = this.getMemberPropertyLists(config);
		if (memberPropertyLists.isNil.not) {
			memberPropertyLists.weightedKeysValuesDo {
				arg key, buildConfig;
				this.buildBranchGroup(m, config, buildConfig);
			};
		};
	}

	/**
	 * Goes through the list of member instances.
	 * Get the list of members, then iterate across its keys. We need to use its
	 * keys because sometimes simple members (such as notifiers) simply need to
	 * have their key defined.
	 */
	buildBranchGroup {
		arg m, config, buildConfig;
		var memberList = config[buildConfig[\sourceKey]];
		var method = buildConfig[\addMethod].asSymbol;
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
				member = this.buildBranchGroupMember(memberConfig, baseConfig, buildConfig, m);
			};
		};
	}

	/**
	 * Build each member. Add a base config for defaults for each.
	 */
	buildBranchGroupMember {
		arg memberConfig, baseConfig, buildConfig, parent;
		var member;

		memberConfig.default(baseConfig);
		memberConfig.default(buildConfig);
		member = this.buildFromConfig(memberConfig, parent);
		member.setConfig(memberConfig);
		member.buildConfig_(buildConfig);
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
