package ch.spacebase.mc.protocol.data.message;

public enum HoverAction {

	SHOW_TEXT("show_text"),
	SHOW_ITEM("show_item"),
	SHOW_ACHIEVEMENT("show_achievement");
	
	private String type;

	private HoverAction(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return this.type;
	}
	
	public static HoverAction byName(String name) {
		for(HoverAction action : values()) {
			if(action.toString().equals(name)) {
				return action;
			}
		}
		
		return null;
	}

}
