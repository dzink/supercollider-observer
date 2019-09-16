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
				subConfig.put(subKey, objects[candidateKey]);
			};
		};
		^ subConfig;
	}

	/**
	 * Return a list of all keys available in this config and all subconfigs,
	 * properly prefixed for access.
	 */
	keys {
		var keys = List[];
		^ this.prKeys(keys, "");
	}

	prKeys {
		arg keys, baseKey = "";
		keys.addAll(objects.keys.collect{
			arg k;
			baseKey ++ k
		});
		objects.keysValuesDo {
			arg key, value;
			if (value.isKindOf(DhConfig)) {
				value.prKeys(keys, baseKey ++ key ++ ".");
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
		default.keys.do {
			arg key;
			if (this.includesKey(key)) {
				var value = default.at(key);
				if (value.isKindOf(DhConfig).not) {
					this.put(key, default.at(key));
				};
			};
		};
		^ this;
	}

	toList {
		var array = List[];
		var keys = objects.keys.asInteger.sort;
		keys.do {

		}
	}

	at {
		arg key;
		var keyArray = this.splitAddress(key);
		^ this.prAt(keyArray);
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
		arg keyArray;
		var key = keyArray.removeAt(0);
		var value = super.at(key);
		if (value.includesKey(key)) {
			^ nil;
		};
		if (keyArray.size == 0) {
			^ value;
		} {
			^ value.prAt(keyArray);
		};
	}

	prPut {
		arg keyArray, value;
		var key = keyArray.removeAt(0);
		if (keyArray.size == 0) {
			super.put(key, value);
		} {
			var subConfig;
			subConfig = super.at(key);
			if (subConfig.isNil()) {
				subConfig = this.class.new();
				super.put(key, subConfig);
			};
			subConfig.prPut(keyArray, value);
		};
	}

	includesKey {
		arg key;
		[\inckey, key].postln;
		^ this.prIncludesKey(this.splitAddress(key));
	}

	prIncludesKey {
		arg keyArray = [];
		var key = keyArray.removeAt(0);
		[\keyArray, keyArray, key].postln;
		if (objects.includesKey(key) and: {this[key].respondsTo(\prIncludesKey)}) {
			^ this[key].prIncludesKey(keyArray);
		} {
			^ true;
		};
		^ false;
	}
}
