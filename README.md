NotificationSystem
  Notifier - receives subscriptions from observeer, emits notifications.
  Observer - subscribes to notifier, observes based on notifications.
	Notification - contains a messageId, and a payload.
	NotificationService - wraps these up.
  Objects have notifiers

Global Observers
Observers can register a key with a notification service. Instead of the observeer subscribing to a single notifier, it selects a
