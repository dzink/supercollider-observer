DhApp : DhModule {
	var < tasks;

	*new{
		^ super.new.init();
	}

	init {
		super.init;
		tasks = DhAtom();
		^ this;
	}

	addTask {
		arg key, task;
		tasks[key] = task;
		task.setTrunk(this);
		task.configure();
		^ this;
	}

	runTasks {
		tasks.weightedKeysValuesDo {
			arg key, task;
			task.run;
		};
	}
}
