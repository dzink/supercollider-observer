/*
 * DhNotifier.sc
 * A class to send notifications to observeers.
 */
DhNotifier {
	var id = 0;
	var <observers = nil;

	notify {
		arg message;
		observers.do {
			arg observer;
			observer.respond(message);
		};
		^ this;
	}

	addObserver {
		arg observer;
		if (observers.isKindOf(List).not) {
			observers = SortedList[];
		};
		observers.add(observer);
		^ this;
	}

}
