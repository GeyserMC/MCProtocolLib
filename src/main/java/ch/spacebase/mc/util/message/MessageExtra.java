package ch.spacebase.mc.util.message;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MessageExtra {

	private JsonObject json;

	public MessageExtra(String str, boolean json) {
		if(json) {
			this.json = new Gson().fromJson(str, JsonObject.class);
		} else {
			this.json = new JsonObject();
			this.json.addProperty("text", str);
			this.json.addProperty("color", ChatColor.WHITE.toString());
		}
	}
	
	public MessageExtra(String text, ChatColor color) {
		this(text, color, null);
	}
	
	public MessageExtra(String text, ChatColor color, List<ChatFormat> formats) {
		this.json = new JsonObject();
		this.json.addProperty("text", text);
		this.json.addProperty("color", color != null ? color.toString() : ChatColor.WHITE.toString());
		if(formats != null) {
			for(ChatFormat format : formats) {
				this.json.addProperty(format.toString(), true);
			}
		}
	}
	
	public String getText() {
		return this.json.get("text").getAsString();
	}
	
	public ChatColor getColor() {
		ChatColor color = ChatColor.byValue(this.json.get("color").getAsString());
		if(color == null) {
			return ChatColor.WHITE;
		}
		
		return color;
	}
	
	public List<ChatFormat> getFormats() {
		List<ChatFormat> ret = new ArrayList<ChatFormat>();
		for(ChatFormat format : ChatFormat.values()) {
			if(this.json.get(format.toString()).getAsBoolean()) {
				ret.add(format);
			}
		}
		
		return ret;
	}
	
	public ClickEvent getClickEvent() {
		if(!this.json.has("clickEvent")) {
			return null;
		}
		
		JsonObject json = this.json.get("clickEvent").getAsJsonObject();
		return new ClickEvent(ClickAction.byValue(json.get("action").getAsString()), json.get("value").getAsString());
	}
	
	public HoverEvent getHoverEvent() {
		if(!this.json.has("hoverEvent")) {
			return null;
		}
		
		JsonObject json = this.json.get("hoverEvent").getAsJsonObject();
		return new HoverEvent(HoverAction.byValue(json.get("action").getAsString()), json.get("value").getAsString());
	}

	public void setClickEvent(ClickEvent event) {
		JsonObject json = new JsonObject();
		json.addProperty("action", event.getAction().toString());
		json.addProperty("value", event.getValue());
		this.json.add("clickEvent", json);
	}

	public void setHoverEvent(HoverEvent event) {
		JsonObject json = new JsonObject();
		json.addProperty("action", event.getAction().toString());
		json.addProperty("value", event.getValue());
		this.json.add("hoverEvent", json);
	}

	protected JsonObject getJson() {
		return this.json;
	}

}
