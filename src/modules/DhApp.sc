DhApp : DhModule {

	*new{
		^ super.new.init();
	}

	init {
		super.init;
		^ this;
	}

	run {
		this.collectTasks();
		this.runTasks();
	}

	runTasks {
		tasks.weightedKeysValuesDo {
			arg key, task;
			task.run;
		};
	}

	configure {
		arg config;
		// var taskConfigs = this.collectTaskConfigs();

	}

	collectTasks {
		var branches = this.getAllBranches();
		branches.do {
			arg branch;
			tasks.putAll(branch.tasks);
		};
		^ this;
	}

}
