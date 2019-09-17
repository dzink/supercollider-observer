DhCacheService[] : DhService {
	var cache;
	doesNotUnderstand {
		arg key ... args;
		if (cache.respondsTo(key)) {}
	}

	
}
