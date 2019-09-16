TestDhNillable : TestDh {
	var n;

	setUp {
		n = DhNillable();
	}

	tearDown {
		n.free;
	}

	test_nillable {
		n[\a] = true;
		this.assert(n[\a], "Object exists.");
		this.assert(n.includesKey(\a), "Object is included.");
		n[\a] = nil;
		this.assert(n[\a].isNil, "Nilled object is still nil but...");
		this.assert(n.includesKey(\a), "Nilled object is included.");
		n.removeAt(\a);
		this.assert(n.includesKey(\a).not, "Removed object is not included.");
		n[\b] = nil;
		this.assert(n[\a].isNil, "Always-nil object can be nil.");
		this.assert(n.includesKey(\b), "Always-nil object is included.");
	}

	test_keys {
		n[\a] = nil;
		this.assert(n.keys.includes(\a), "Keys includes nil object.");
	}
}
