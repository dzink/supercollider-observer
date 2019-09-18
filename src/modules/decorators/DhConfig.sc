DhConfig : DhDependencyInjectionContainer {

	*fromYaml {
		arg yaml;
		var yamlConfig = yaml.parseYAML();
		^ DhConfig.from(yamlConfig).convertTypes();
	}

	*fromYamlFile {
		arg yamlFile;
		var yamlConfig = yamlFile.parseYAMLFile();
		^ DhConfig.from(yamlConfig).convertTypes();
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

				this.put(baseKey ++ key, value);
			};
		};
		^ this;
	}

	convertTypes {
		var keys = this.keys;
		keys.do {
			arg key;
			var value = this.safeAt(key);
			if (value.isKindOf(String)) {
				this.put(key, this.prConvertStringType(value))
			};
		};
	}

	prConvertStringType {
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
	 * Return a list of keys for the top level of config only.
	 */
	topKeys {
		^ super.keys;
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
		this.topKeys.do {
			arg key;
			var value = this.safeAt(key);
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
		var emptyKeys = this.prFindFreeDefaultKeys(default);

		emptyKeys.do {
			arg key;
			this[key] = default.safeAt(key);
		};
		^ this;
	}

	prFindFreeDefaultKeys {
		arg default;
		var myKeys = this.fullKeys(), theirKeys = default.keys(), emptyKeys, myEndPoints;
		// Starting off by eliminating duplicate keys from default right off.
		emptyKeys = theirKeys.difference(myKeys);
		myEndPoints = this.keys();

		emptyKeys = emptyKeys.reject {
			arg emptyKey;
			this.prFindFreeDefaultKeyBase(myEndPoints, emptyKey);
		};

		^ emptyKeys;
	}

	/**
	 * Find if a candidate key can be written without overwriting other data.
	 */
	prFindFreeDefaultKeyBase {
		arg myEndPoints, emptyKey;
		emptyKey = emptyKey.asString();
		myEndPoints.do {
			arg myEndPoint;
			myEndPoint = myEndPoint.asString;
			if (emptyKey.beginsWith(myEndPoint)) {
				^ true;
			};
		};
		^ false;
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
		^ this.fullKeys.includes(key.asSymbol);
	}

	/**
	 * Anything that is "base" or ends in ".base" is a requirement.
	 */
	getBases {
		var topKeys = this.keys.select({
			arg key;
			(key.asSymbol == \base or: {
				key.asString.endsWith(".base");
			});
		});
		topKeys = topKeys.collect({
			arg key;
			[key, this[key].asSymbol];
		});
		^ DhAtom.newFrom(topKeys.flatten);
	}

	/**
	 * Given a Dictionary of configs, place the defaults where this config has the
	 * key *.base.
	 */
	addBaseDefaults {
		arg configs;
		this.getBases.keysValuesDo {
			arg key, base;
			if (key == \base) {
				this.default(configs[base]);
			} {

				// Is this REALLY the best way to do a substring in sclang???
				var configKey = key.asString.copyRange(0, key.asString.size -6);
				this[configKey].default(configs[base]);
			};
		};
	}

	storeItemsOn { | stream |
		var addComma = false;
		var keys = this.keys;
		stream << "\n";
		keys.sort.do {
			arg key;
			var item = this.at(key);
			if (stream.atLimit) { ^ this };
			stream << "  ";
			key.storeOn(stream);
			stream << " -> ";
			item.storeOn(stream);
			stream << ",\n";
		};
	}

	printItemsOn { | stream |
		var addComma = false;
		var keys = this.keys;
		keys.sort.do {
			arg key;
			var item = this.at(key);
			if (stream.atLimit) { ^ this };
			key.printOn(stream);
			stream << " -> ";
			item.printOn(stream);
			stream.comma.space;
		};
	}

	sortKeysByProperty {
		arg property = \weight;
		var keys = this.topKeys;
		^ keys.asArray.sortMap {
			arg key;
			var weight = this.determineWeight(this[key], property);
			weight;
		};
	}
}
