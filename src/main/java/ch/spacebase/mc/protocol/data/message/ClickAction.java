package ch.spacebase.mc.protocol.data.message;

public enum ClickAction {

	RUN_COMMAND("run_command"),
	SUGGEST_COMMAND("suggest_command"),
	OPEN_URL("open_url"),
	OPEN_FILE("open_file");
	
	private String type;

	private ClickAction(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
	
	public static ClickAction byName(String name) {
		for(ClickAction action : values()) {
			if(action.toString().equals(name)) {
				return action;
			}
		}
		
		return null;
	}

}
