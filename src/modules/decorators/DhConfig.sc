DhConfig : DhDependencyInjectionContainer {

	at {
		arg key;
		^ super.at(key);
	}

	subConfig {
		arg key;
		var subConfig = DhConfig();
		var keyString = key.asString ++ ".";
		objects.keys.do {
			arg candidateKey;
			candidateKey = candidateKey.asString;
			if (candidateKey.beginsWith(keyString)) {
				var subKey = candidateKey.drop(keyString.size);
				subConfig.put(subKey, objects[candidateKey]);
			};
		};
		^ subConfig;
	}

	default {
		arg default;
		default.keysValuesDo {
			arg key, value;
			if (objects.at(key).isNil) {
				objects.put(key, value);
			};
		};
	}
}

	// at {
	// 	arg key;
	// 	var keyArray = this.splitAddress(key);
	// 	^ this.prAt(keyArray);
	// }
	//
	// put {
	// 	arg key, value;
	// 	var keyArray = this.splitAddress(key);
	// 	this.prPut(keyArray, value);
	// 	^ this;
	// }
	//
	// splitAddress {
	// 	arg key;
	// 	var keyArray = key.asString.split($.);
	// 	^ keyArray.collect {
	// 		arg key;
	// 		key.asSymbol;
	// 	};
	// }
	//
	// prAt {
	// 	arg keyArray;
	// 	var key = keyArray.removeAt(0);
	// 	var value = super.at(key);
	// 	if (value.isNil) {
	// 		^ nil;
	// 	};
	// 	if (keyArray.size == 0) {
	// 		^ value;
	// 	} {
	// 		^ value.prAt(keyArray);
	// 	};
	// }
	//
	// prPut {
	// 	arg keyArray, value;
	// 	var key = keyArray.removeAt(0);
	// 	if (keyArray.size == 0) {
	// 		super.put(key, value);
	// 	} {
	// 		var subConfig;
	// 		subConfig = super.at(key);
	// 		if (super.at(key).isNil) {
	// 			subConfig = this.class.new();
	// 			super.put(key, subConfig);
	// 		};
	// 		subConfig.prPut(keyArray, value);
	// 	};
	// }
	//
	// /**
	//  * Merges another default config into this one.
	//  * Recursively passes subConfigs into subConfigs. Anywhere where this has
	//  * a nil value where the default has a value, replace with default.
	//  */
	// default {
	// 	arg config;
	// 	this.prDefault(config, []);
	// 	^ this;
	// }
	//
	// prDefault {
	// 	arg config, keyArray = List[];
	// 	config.keysValuesDo {
	// 		arg key, defaultSubConfig;
	// 		var newKeyArray = List.newFrom(keyArray).add(key);
	// 		var subConfig = super.at(key);
	//
	// 		if (subConfig.isNil) {
	// 			var newSubConfig;
	//
	// 			// Only DhConfig data types are shallow copied.
	// 			if (defaultSubConfig.isKindOf(DhConfig)) {
	// 				newSubConfig = defaultSubConfig.copy();
	// 			} {
	// 			 	newSubConfig = defaultSubConfig;
	// 			};
	// 			this.prPut([key], newSubConfig);
	// 		} {
	//
	// 			// Continue searching recursively.
	// 			if (subConfig.isKindOf(DhConfig)) {
	// 				subConfig.prDefault(defaultSubConfig, newKeyArray);
	// 			}
	// 		};
	// 	};
	// 	^ this;
	// }
// }
