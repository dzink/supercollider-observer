/**
 * A list of keys which when accessed, access the dependency injector.
 * This can be used to keep references to objects that are local to context,
 * while still maintaining the power of a central DIC.
 * This class is merely a map. The objects are evaluated by the DIC on access.
 */
DhDependencyInjectorSubset[] : DhAtom {
	var parent, < baseKey;

	*new {
		arg parent, baseKey = '';
		^ super.new.init(parent, baseKey);
	}

	init {
		arg otherParent, otherBaseKey;
		super.init();
		parent = otherParent;
		baseKey = otherBaseKey;
		^ this;
	}

	at {
		arg key;
		^ parent[this.convertKey(key)];
	}

	safeAt {
		arg key;
		^ parent.safeAt[this.convertKey(key)];
	}

	put {
		arg key, function;
		var pKey = this.convertKey(key);
		parent.put(this.convertKey(pKey), function);
		super.put(pKey);
	}

	baseKey_ {
		arg key;
		key = key.asString;
		// @TODO update the existing strings.
		// this.keys.do {
		// 	arg testKey;
		// 	if (testKey.asString.beginsWith(baseKey)) {
		// 		this.put
		// 	}
		// };
		baseKey = key;
		^ this;
	}

	evaluatedAt {
		arg key;
		^ parent.evaluatedAt(this.convertKey(key));
	}

	convertKey {
		arg key;
		^ (baseKey +/+ key);
	}
}
