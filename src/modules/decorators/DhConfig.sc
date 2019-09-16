DhConfig : DhDependencyInjectionContainer {

	*fromYaml {
		arg yaml;
		var yamlConfig = yaml.parseYAML();
		^ DhConfig.from(yamlConfig, IdentityDictionary[
			\convertTypes -> true,
		]);
	}

	*fromYamlFile {
		arg yamlFile;
		var yamlConfig = yamlFile.parseYAMLFile();
		^ DhConfig.from(yamlConfig, IdentityDictionary[
			\convertTypes -> true,
		]);
	}

	*from {
		arg object, options;
		var c = super.new();
		^ c.prParseObject("", object, options);
		// ^ DhConfig.fromObject(object);
	}

	prParseObject {
		arg baseKey = "", object = [], options = IdentityDictionary[];
		object.keysValuesDo {
			arg key, value;
			var convertTypes = options[\convertTypes] ?? false;

			// Convert arrays to dictionary with indices as keys.
			if (value.isKindOf(Array)) {
				value = Dictionary.newFrom([(0..(value.size - 1)), value].lace);
			};
			if (value.isKindOf(Dictionary)) {
				this.prParseObject(baseKey ++ key ++ ".", value, options);
			} {
				if (convertTypes) {
					value = this.convertTypes(value);
				};
				this.put(baseKey ++ key, value);
			};
		};
		^ this;
	}

	convertTypes {
		arg value;
		if ("^-*[\\d\\.]$".matchRegexp(value)) {
			^ value.asFloat;
		};
		if ("true".compare(value, true) == 0) {
			^ true;
		};
		if ("false".compare(value, true) == 0) {
			^ false;
		};
		if ("nil".compare(value, true) == 0) {
			^ nil;
		};
		if ("null".compare(value, true) == 0) {
			^ nil;
		};
		^ value;
	}

	subConfig {
		arg key;
		var subConfig = DhConfig();
		var keyString = key.asString ++ ".";
		this.keys.do {
			arg candidateKey;
			candidateKey = candidateKey.asString;
			if (candidateKey.beginsWith(keyString)) {
				var subKey = candidateKey.drop(keyString.size);
				subConfig.put(subKey, this[candidateKey]);
			};
		};
		^ subConfig;
	}

	/**
	 * Return a list of all the keys which point to non-config data.
	 */
	keys {
		var keys = List[];
		^ this.prKeys(keys, "", false);
	}

	/**
	 * Return a list of all keys available in this config and all subconfigs,
	 * properly prefixed for access.
	 */
	fullKeys {
		var keys = List[];
		^ this.prKeys(keys, "", true);
	}

	prKeys {
		arg keys, baseKey = "", fullKeys = false;
		this.keysValuesDo {
			arg key, value;
			if (value.isKindOf(DhConfig)) {
				value.prKeys(keys, baseKey ++ key ++ ".", fullKeys);
				if (fullKeys) {
					keys.add((baseKey ++ key).asSymbol);
				}
			} {
				keys.add((baseKey ++ key).asSymbol);
			};
		};
		^ keys;
	}

	/**
	 * Adds default config to an existing config. The default will only add
	 * where this config is empty.
	 */
	default {
		arg default;
		var myKeys = this.fullKeys();
		var theirKeys = default.keys();
		var emptyKeys = theirKeys.difference(myKeys);
		emptyKeys.postln;
		// default.keys.do {
		// 	arg key;
		// 	if (myKeys.includes(key).not) {
		// 		var value = default.at(key);
		// 		if (value.isKindOf(DhConfig).not) {
		// 			this.put(key, default.at(key));
		// 		};
		// 	};
		// };
		^ this;
	}

	prDefault {
		arg default, baseKey = "", myKeys = [];
		this.keysValuesDo {
			arg key, value;
			var candidateKey = (baseKey ++ key).asSymbol;
			if (myKeys.includes(candidateKey).not) {

			}
		}
	}

	toList {
		var array = List[];
		var keys = this.keys.asInteger.sort;
		keys.do {
			arg key;

			// @TODO do I want at, or safeAt? Seems like if we're getting a list,
			// objects should be evaluated...
			array = array.add(this.at(key));
		};
		^ array.asArray();
	}

	at {
		arg key;
		var keyArray = this.splitAddress(key);
		^ this.prAt(keyArray, true);
	}

	safeAt {
		arg key;
		var keyArray = this.splitAddress(key);
		^ this.prAt(keyArray, false);
	}

	put {
		arg key, value;
		var keyArray = this.splitAddress(key);
		this.prPut(keyArray, value);
		^ this;
	}

	splitAddress {
		arg key;
		var keyArray = key.asString.split($.);
		^ keyArray.collect {
			arg key;
			key.asSymbol;
		};
	}

	prAt {
		arg keyArray, evaluate = true;
		var key = keyArray.removeAt(0);
		var value;
		value = if (evaluate) { super.at(key) } { super.safeAt(key) };
		if (keyArray.size == 0) {
			^ value;
		} {
			if (value.respondsTo(\prAt)) {
				^ value.prAt(keyArray, evaluate);
			} {
				^ nil;
			};
		};
	}

	prPut {
		arg keyArray, value;
		var key = keyArray.removeAt(0);
		if (keyArray.size == 0) {
			super.put(key, value);
		} {
			var subConfig;
			subConfig = this.safeAt(key);
			if (subConfig.isKindOf(DhConfig).not) {
				subConfig = this.class.new();
				super.put(key, subConfig);
			};
			subConfig.prPut(keyArray, value);
		};
	}

	includesKey {
		arg key;
		^ this.keys.includes(key);
	}

	prIncludesKey {
		arg keyArray = [];
		var key = keyArray.removeAt(0);
		if (super.includesKey(key) and: {this.safeAt(key).respondsTo(\prIncludesKey)}) {
			^ this[key].prIncludesKey(keyArray);
		} {
			^ true;
		};
		^ false;
	}
}
