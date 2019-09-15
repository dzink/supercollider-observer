/**
 * Tracks observations for a given notifier. If all
 */
DhAsyncResolver : Condition {
	var <count = 0;
	*new {
		var r = super.new();
		^ r.init();
	}

	init {
		this.test = { this.count <= 0 };
	}

	increment {
		arg n;
		count = count + n;
	}

	decrement {
		arg n;
		count = count - n;
		if (count <= 0) {
			this.unhang();
		};
	}

}
