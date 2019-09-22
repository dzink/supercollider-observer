DhResponse : DhNillable {
	var <> scheduler;

	*new {
		^ super.new.init();
	}

	init {
		super.init();
		^ this;
	}

	/**
	 * Returns whatever you pass into it, once its scheduler finishes all queued
	 * threads.
	 */
	resolveQueue {
		scheduler.waitForQueued();
		^ this;
	}

	/**
	 * Returns whatever you pass into it, once its scheduler finishes all active
	 * threads.
	 */
	resolve {
		scheduler.waitForAll();
		^ this;
	}

	/**
	 * Responds asynchronusly to this result once all are resolved.
	 */
	respond {
		arg func;
		fork {
			this.resolve();
			func.value(this);
		};
		^ this;
	}

}
