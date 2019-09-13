/*
 * DhNotifier.sc
 * A class to send notifications to observeers.
 */
DhNotifer {
	id = 0;
	observers = List[];
	notify {
		arg message;
		observers.do {
			arg observer;
			observer.observe(message);
		};
	}
}
