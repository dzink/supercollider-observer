TestDhCache : TestDh {
	var cache, c;

	setUp {
		cache = DhCache();
	}

	tearDown {
		cache.free;
		c.free;
	}

	test_at {
		cache[\cat] = \meow;
		this.assertEquals(cache[\cat], \meow, "Basic put/get works");
	}

	test_cacheAside {
		cache[\cat] = \meow;
		c = cache.cacheAside(\cat);
		this.assertEquals(c, \meow, "Retrieve works without default");
	}

	test_default {
		c = cache.cacheAside(\cat, {\meow});
		this.assertEquals(c, \meow, "Retrieve works with default");
	}

	test_defaultThenRetrieve {
		c = cache.cacheAside(\cat, {\meow});
		c = cache.cacheAside(\cat, {\woof});
		this.assertEquals(c, \meow, "Retrieve default just once, then get previous default.");
	}

	test_nilWithDefault {
		var key = \cat;
		cache.put(key, nil);
		c = cache.cacheAside(key, {\woof});
		this.assert(cache.includesKey(key), "Valid keys are set on nil.");
		this.assertEquals(c, nil, "Nil values are cached.");
	}

	test_clearAt {
		cache.cacheAside(\a, { \aa });
		cache.cacheAside(\b, { \bb });
		cache.cacheAside(\ab, { \bb });
		cache.cacheAside(\abc, { nil });
		this.assertEquals(cache.size, 4, "Cache is full of objects.");
		cache.clearAt(\b);
		this.assertEquals(cache.size, 3, "Cache clears one object.");
		cache.clearAt("?b*");
		this.assertEquals(cache.size, 1, "Cache clears wildcard objects.");
	}

	test_clearAll {
		cache.cacheAside(\a, { \aa });
		cache.cacheAside(\b, { \bb });
		cache.cacheAside(\ab, { \bb });
		cache.cacheAside(\abc, { nil });
		this.assertEquals(cache.size, 4, "Cache is full of objects.");
		cache.clearAll();
		this.assertEquals(cache.size, 0, "Cache clears all objects.");
	}


}
