NotificationSystem
  Notifier - receives subscriptions from observeer, emits notifications.
  Observer - observes a notifier, performs actions based on notifications.
	Notification - contains a messageId, and a payload.
	NotificationService - wraps these up.
  Objects have notifiers

Global Observers
Observers can register a key with a notification service. Instead of the observeer subscribing to a single notifier, it selects a

#Usage

var assembler, factory, configs, root;
assembler = DhConfigAssembler();
assembler.compilePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/compiled/";
assembler.sourcePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/";
configs = assembler.assemble();
factory = DhAbstractFactory(configs);

root = factory.buildKindOf(\\helloWorld);
root.moduleInit();
// root.methods.postln;
// root.config.postcs;
root.getService(\\server).address.postln;
root.selectBranches.do({
  arg branch;
  // branch.config.postln;
  this.assert(branch.config.size > 0, "Config is not empty for " ++ branch.address);
});
// \\waiting.postln;
// 3.wait;
// root.observers.address.postln;
// root.getService('logger').warn("Bab bba");
// root.log("where's that log");
// root.log("hiding", 12);
// root.warn("you didn't see");
// root.error("now you will pay");
// root.selectBranches({
// 	arg branch;
// 	branch.isKindOf(DhService);
// }).do({
// 	arg branch;
// 	branch.serviceInit;
// });
//
root.getService(\server).methods[\serviceStart].value();
// factory.builtMembers.do {
// 	arg m;
// 	m.address.postln;
// };
//
// f.configs = configs;
// root = f.build(configs[\test]);
// f.builtMembers.do {
// 	arg member;
// };
