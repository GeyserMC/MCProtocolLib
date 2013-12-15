package ch.spacebase.mc.util.message;

public class ClickEvent {

	private ClickAction action;
	private String value;
	
	public ClickEvent(ClickAction action, String value) {
		this.action = action;
		this.value = value;
	}
	
	public ClickAction getAction() {
		return this.action;
	}
	
	public String getValue() {
		return this.value;
	}
	
}
