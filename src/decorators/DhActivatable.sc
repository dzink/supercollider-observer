DhActivatable {
	var <>active = true;
	var timeGoal;

	toggle {
		active = active.asBoolean().not;
	}

	isActive {
		^ active;
	}

	pauseUntil {
		arg newTime;
		// @TODO
	}

}
