NotificationSystem
  Notifier - receives subscriptions from observeer, emits notifications.
  Observer - observes a notifier, performs actions based on notifications.
	Notification - contains a messageId, and a payload.
	NotificationService - wraps these up.
  Objects have notifiers

Global Observers
Observers can register a key with a notification service. Instead of the observeer subscribing to a single notifier, it selects a

#Discovery
- All objects share a single dependency injection container.
- Object addresses are [parent]/[parent]/.../[self].
- Objects can be added as objects.
- Functions can be added as functions. When the container accesses that dependency, the function will be run, and the dependency will be stored in its place.
- The DIC can also indicate that a member is a factory; in this case, the function will be evaluated on access, but it will not be stored. This is useful for storing code that will generate multiple similar objects without having to run it before it is used. This saves time if objects are never used.

#Services
- Services are shared between different objects.
- Only plugins can store services.
- Services are shared downward. Each plugin's descendants will also be able to use that service.
- A plugin's descendant can override a service for it and its descendants, but not for its ancestors.

#Usage
```
({
  var assembler, factory, configs, root;

  // Prepare to build objects.
  assembler = DhConfigAssembler();
  assembler.compilePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/compiled/";
  assembler.sourcePath = "/Users/danzinkevich/Library/Application Support/SuperCollider/hooks/core/src/";
  configs = assembler.assemble();
  factory = DhAbstractFactory(configs);

  // Begin building objects.
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
})
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
```
Create a server
Create a node manager
Create a

root
  server
  controllerMap
  node manager
    observes controller
