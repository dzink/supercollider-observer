TestDhService : TestDh {
	var s;

	setUp {
		s = DhService();
	}

	tearDown {
		s.free;
	}

	addFunction {
		
	}
}
