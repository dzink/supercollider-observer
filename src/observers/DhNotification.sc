DhNotification : DhActivatable{
	var <message = nil;
	var <context = nil;

	*new {
		arg message = nil, context = nil;
		var m = super.new();
		^ m.init(message, context);
	}

	init {
		arg mess, cont;
		message = mess;
		context = cont;
		^ this;
	}

}
