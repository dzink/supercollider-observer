DhApp : DhModule {
	var < tasks;

	*new{
		^ super.new.init();
	}

	init {
		super.init;
		\imanapp.postln;
		tasks = DhAtom();
		^ this;
	}

	addTask {
		arg key, task;
		// task.setConfig(config);
		// task.buildConfig = buildConfig;
		tasks[key] = task;
		task.setTrunk(this);
		task.configure();
		^ this;
	}

	runTasks {
		tasks.weightedKeysValuesDo {
			arg key, task;
			[\doatask, task, task.id].postln;
			task.run;
		};
	}
}
