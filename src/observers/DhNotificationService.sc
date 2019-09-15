/**
 * DhNotificationService
 * Controls handshakes between notifiers.
 * Registers Glo
 */

DhNotificationService : DhService {
	var <notifiers;

	registerNotifier {
		arg key, notifier;
		this.put(key, notifier);
	}

	at {
		arg key;
		^ notifiers[key];
	}

	put {
		arg key, value;
		notifiers.put(key, value);
		^ this;
	}

	notify {
		arg key, message;
		notifiers[key].respond(message);
	}
}
