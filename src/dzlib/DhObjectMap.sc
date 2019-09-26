DhObjectMap {
	var addresses;

	*new {
		^ super.new.init();
	}

	init {
		// cache = DhCache();
		addresses = IdentityDictionary();
		// super.init();
		^ this;
	}

	find {
		arg address, startAt = nil;
		var object;
		var addressString = address.asString;
		if (addressString.beginsWith("./")) {
			var firstKeys, lastKeys;
			addressString = addressString.copyRange(2, addressString.size);
			address = startAt.getAddress().asString +/+ addressString;
		};
		object = addresses[address.asSymbol];
		if (object.isNil.not) {
			^ object;
		};
		^ nil;
	}

	traceParents {
		// arg address;
		// var addressArray = address.split($/).collect({ arg d; d.asSymbol}).asList;
		// var newaddress;
		// var i;
		// while ({ (i = addressArray.indexOf('..')).isNil.not }) {
		// 	i.postln;
		// 	addressArray.removeAt(i);
		// 	addressArray.removeAt(i - 1);
		// };
		// newAddress = addressArray.join($/);
		// if (newAddress.)
	}

	register {
		arg object;
		addresses[object.getAddress().asSymbol] = object;
		^ this;
	}

	removeAt {
		arg key;
		addresses.removeAt(key.asSymbol);
		^ this;
	}

	includes {
		arg key;
		^ addresses.includes(key.asSymbol);
	}

	list {
		arg wildcard;
		// @TODO use wildcard.
		^ addresses.keys;
	}
}
