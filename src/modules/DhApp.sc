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
		this.configureTasks();
		this.runTasks();
	}

	runTasks {
		var a = DhAtom();
		tasks.weightedKeysValuesDo {
			arg key, task;
			// [\task, key, tasks.class, task.config !? _.at(\weight), a.determineWeight(task)].postln;
			task.run;
		};
	}

	configureBranches {
		arg class;
		var branches = this.selectBranches({
			arg branch;
			branch.isKindOf(class);
		});
		branches.do {
			arg branch;
			// [\configuring, branch.getAddress].postln;
			branch.configure(branch.config);
		};
		^ this;
	}

	configureTasks {
		this.configureBranches(DhTask);
		^ this;
	}

	configureNotifiers {
		this.configureBranches(DhNotifier);
		^ this;
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
