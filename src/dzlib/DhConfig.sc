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

	/**
	 * Converts an object into a DhConfig.
	 */
	prParseObject {
		arg baseKey = "", object = [], options = IdentityDictionary[];
		object.keysValuesDo {
			arg key, value;

			// Convert arrays to DhConfig with indices as keys.
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

	/**
	 * Converts scalar values based on *prConvertStringType.
	 */
	convertTypes {
		var keys = this.keys;
		keys.do {
			arg key;
			var value = this.safeAt(key);
			if (value.isKindOf(String)) {
				value = DhConfig.prConvertStringType(value);
				this.put(key, value);
			};
		};
	}

	*prConvertStringType {
		arg value;
		if ("^-*[\\d\\.]*$".matchRegexp(value)) {
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
		if (DhConfig.prStringIsFunction(value)) {
			^ value.compile;
		}

		^ value;
	}

	*prStringIsFunction {
		arg value;
		if (value.beginsWith("{").not) {
			^ false;
		};
		if (value.endsWith("}").not) {
			^ false;
		};
		if (value.findAll("{").size != value.findAll("}").size) {
			^ false;
		};
		^ true;
	}

	/**
	 * Returns a portion of this config, starting at key.
	 */
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

	/**
	 * Traverses objects, assembling a list of keys.
	 */
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
			this.put(key, default.safeAt(key));

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

	/**
	 * Given a string like a.b.c, return the config at that address.
	 */
	at {
		arg key;
		var keyArray = this.splitAddress(key);
		^ this.prAt(keyArray, true);
	}

	/**
	 * Given a string like a.b.c, return the config at that address. Do not
	 * evaluate.
	 */
	safeAt {
		arg key;
		var keyArray = this.splitAddress(key);
		^ this.prAt(keyArray, false);
	}

	/**
	 * Given a string like a.b.c, return the config at that address.
	 */
	put {
		arg key, value;
		var keyArray = this.splitAddress(key);

		this.prPut(keyArray, value);
		^ this;
	}

	/**
 	 * Given a string like a.b.c, separate it into its component addresses in an
	 * array.
 	 */
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

		// // If an empty key is passed in, then return this.
		if (key == "".asSymbol) {
			^ this;
		};

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

	removeAt {
		arg key;
		var keyArray = this.splitAddress(key);
		this.prRemoveAt(keyArray);
		^ this;
	}

	prRemoveAt {
		arg keyArray;
		if (keyArray.size == 1) {
			var key = keyArray.removeAt(0);
			super.removeAt(key);
		} {
			var key = keyArray.removeAt(0);
			if (this.includesKey(key)) {
				var object = this.safeAt(key);
				if (object.isKindOf(DhConfig)) {
					object.prRemoveAt(keyArray);
					if (object.size == 0) {
						super.removeAt(key);
					};
				};
			}
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
				this.put(key, subConfig);
			};
			subConfig.prPut(keyArray, value);
		};
	}

	includesKey {
		arg key;
		^ this.fullKeys.includes(key.asSymbol);
	}

	/**
	 * Get valid compile strings with all the evaluation.
	 */
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
			var weight = this.determineWeight(this.safeAt(key), property);
			weight;
		};
	}

	/**
	 * Anything that is "base" or ends in ".base" is a requirement.
	 */
	getBaseKeys {
		^ this.selectKeysEndingIn(\base);
	}

	/**
	 * Given a Dictionary of configs, place the defaults where this config has the
	 * key *.base.
	 */
	addBaseDefaults {
		arg configs;
		this.getBaseKeys.do {
			arg key;
			var base = this.safeAt(key);
			var configKey = key.asString.copyRange(0, key.asString.size -6);
			this[configKey].default(configs[base]);
			this.removeAt(key);
		};
	}

	applyIncludes {
		var includeKeys = this.getIncludeKeys();
		var basePath = this[\configBasePath] ?? "/";

		// A list of the keys that end in *include, or include itself.
		includeKeys.do {
			arg key;
			var config = this[key];
			var baseKey = key.asString;
			baseKey = baseKey.keep(baseKey.size - 7);

			// A typical config item looks like [server -> ./include/server.sc].
			config.keysValuesDo {
				arg key, path;
				if (path.isKindOf(String)) {
					if (path.beginsWith(".")) {
							path = basePath +/+ path;
						};
						this.include(baseKey, path);
					};
			};
			this.removeAt(key);
		};
		^ this;
	}


	include {
		arg baseKey, path;
		var loadedConfig;
		loadedConfig = path.load;
		if (loadedConfig.isKindOf(DhConfig)) {
			this[baseKey].default(loadedConfig);
		};
	}

	getIncludeKeys {
		^ this.selectKeysEndingIn(\include);
	}

	selectKeysEndingIn {
		arg key;
		var keys = DhWildcard.wildcardMatchAll(this.fullKeys, "*." ++ key);
		if (this.includesKey(key)) {
			keys.add(key);
		};
		^ keys;
	}


}
