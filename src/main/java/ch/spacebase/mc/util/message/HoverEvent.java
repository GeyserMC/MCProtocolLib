package ch.spacebase.mc.util.message;

public class HoverEvent {

	private HoverAction action;
	private String value;
	
	public HoverEvent(HoverAction action, String value) {
		this.action = action;
		this.value = value;
	}
	
	public HoverAction getAction() {
		return this.action;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
