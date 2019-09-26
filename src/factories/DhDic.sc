DhDic : DhService {
	var dic;

	*new {
		var d = super.new();
		^ d.init;
	}

	init {
		super.init();
		^ this;
	}
}
