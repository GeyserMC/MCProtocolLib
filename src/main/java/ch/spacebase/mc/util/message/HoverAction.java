package ch.spacebase.mc.util.message;

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
	
	public static HoverAction byValue(String val) {
		for(HoverAction action : values()) {
			if(action.toString().equals(val)) {
				return action;
			}
		}
		
		return null;
	}

}
