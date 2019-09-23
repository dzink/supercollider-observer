/**
 * DhNotificationService
 * Controls handshakes between notifiers.
 * Registers Glo
 */

DhNotificationService[] : DhService {
	var < notifiers;

	*new {
		^ super.new.init();
	}

	init {
		super.init();
		notifiers = DhDependencyInjectionContainer();
		^ this;
	}

	addNotifier {
		arg key, notifier;
		notifiers.put(key, notifier);
		notifier.setTrunk(this);
	}

	at {
		arg key;
		^ notifiers[key];
	}

	put {
		arg key, value;
		^ this.addNotifier(key, value);
	}

	notify {
		arg key, message;
		notifiers[key].notify(message);
	}
}
