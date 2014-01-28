package ch.spacebase.mc.protocol.data.message;

import java.util.ArrayList;
import java.util.List;

public class MessageStyle implements Cloneable {

	private ChatColor color = ChatColor.WHITE;
	private List<ChatFormat> formats = new ArrayList<ChatFormat>();
	private ClickEvent click;
	private HoverEvent hover;
	private String insertion;
	private MessageStyle parent;
	
	public MessageStyle() {
	}
	
	public MessageStyle(MessageStyle parent) {
		this.parent = parent;
	}
	
	public boolean isDefault() {
		return this.color == ChatColor.WHITE && this.formats.isEmpty() && this.click == null && this.hover == null && this.insertion == null;
	}
	
	public ChatColor getColor() {
		return this.color;
	}
	
	public List<ChatFormat> getFormats() {
		return new ArrayList<ChatFormat>(this.formats);
	}
	
	public ClickEvent getClickEvent() {
		return this.click;
	}
	
	public HoverEvent getHoverEvent() {
		return this.hover;
	}
	
	public String getInsertion() {
		return this.insertion;
	}
	
	public MessageStyle getParent() {
		return this.parent != null ? this.parent : new MessageStyle();
	}
	
	public MessageStyle setColor(ChatColor color) {
		this.color = color;
		return this;
	}
	
	public MessageStyle setFormats(List<ChatFormat> formats) {
		this.formats = new ArrayList<ChatFormat>(formats);
		return this;
	}
	
	public MessageStyle addFormat(ChatFormat format) {
		this.formats.add(format);
		return this;
	}
	
	public MessageStyle removeFormat(ChatFormat format) {
		this.formats.remove(format);
		return this;
	}
	
	public MessageStyle clearFormats() {
		this.formats.clear();
		return this;
	}
	
	public MessageStyle setClickEvent(ClickEvent event) {
		this.click = event;
		return this;
	}
	
	public MessageStyle setHoverEvent(HoverEvent event) {
		this.hover = event;
		return this;
	}
	
	public MessageStyle setInsertion(String insertion) {
		this.insertion = insertion;
		return this;
	}
	
	protected MessageStyle setParent(MessageStyle parent) {
		this.parent = parent;
		return this;
	}
	
	@Override
	public String toString() {
		return "MessageStyle{color=" + this.color + ",formats=" + this.formats + ",clickEvent=" + this.click + ",hoverEvent=" + this.hover + ",insertion=" + this.insertion + "}";
	}
	
	@Override
	public MessageStyle clone() {
		return (this.parent != null ? new MessageStyle(this.parent) : new MessageStyle()).setColor(this.color).setFormats(this.formats).setClickEvent(this.click != null ? this.click.clone() : null).setHoverEvent(this.hover != null ? this.hover.clone() : null).setInsertion(this.insertion);
	}
	
}
