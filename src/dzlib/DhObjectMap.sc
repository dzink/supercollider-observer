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
		address = this.standardizeAddress(address, startAt);
		object = addresses[address];
		if (object.isNil.not) {
			^ object;
		};
		^ nil;
	}

	standardizeAddress {
		arg address, startAt;
		var addressString = address.asString;

		if (addressString.contains("..")) {
			if (addressString.beginsWith("./")) {
				addressString = addressString.copyRange(1, addressString.size);
			};
			addressString = startAt.getAddress().asString +/+ addressString;
			addressString = this.standardizeParents(addressString);
		};
		if (addressString.beginsWith("./")) {
			addressString = addressString.copyRange(2, addressString.size);
			addressString = startAt.getAddress().asString +/+ addressString;
		};
		if (addressString.beginsWith("/")) {
			addressString = startAt.getRoot().getAddress().asString +/+ addressString;
		};
		if (addressString.compare(".") == 0) {
			addressString = startAt.getAddress().asString;
		};

		^ this.trimAddress(addressString).asSymbol;
	}

	trimAddress {
		arg address;
		^ address.asString.split($/).reject({arg a; a.size == 0}).join("/");
	}

	standardizeParents {
		arg address;
		var addressArray = address.split($/).collect({ arg d; d.asSymbol}).asList;
		var newAddress;
		var i;
		while ({ (i = addressArray.indexOf('..')).isNil.not }) {
			addressArray.removeAt(i);
			addressArray.removeAt(i - 1);
		};
		newAddress = addressArray.join($/);
		^ newAddress;
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
