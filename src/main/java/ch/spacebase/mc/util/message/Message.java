package ch.spacebase.mc.util.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Message {

	private JsonObject json;
	
	public Message(String text) {
		this(text, false);
	}
	
	public Message(String str, boolean tryJson) {
		if(tryJson) {
			try {
				this.json = new Gson().fromJson(str, JsonObject.class);
				return;
			} catch(Exception e) {
			}
		}
		
		this.json = new JsonObject();
		this.json.addProperty("text", str);
	}
	
	public Message(String text, ChatColor color) {
		this(text, color, null);
	}
	
	public Message(String text, ChatColor color, List<ChatFormat> formats) {
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
		return this.json.has("text") ? this.json.get("text").getAsString() : null;
	}
	
	public String getTranslate() {
		return this.json.has("translate") ? this.json.get("translate").getAsString() : null;
	}
	
	public Message[] getTranslateWith() {
		return this.json.has("with") ? this.arrayToMessages(this.json.get("with").getAsJsonArray()) : null;
	}
	
	public ChatColor getColor() {
		if(!this.json.has("color")) {
			return ChatColor.WHITE;
		}
		
		ChatColor color = ChatColor.byValue(this.json.get("color").getAsString());
		if(color == null) {
			return ChatColor.WHITE;
		}
		
		return color;
	}
	
	public void setColor(ChatColor color) {
		this.json.addProperty("color", color.toString());
	}
	
	public List<ChatFormat> getFormats() {
		List<ChatFormat> ret = new ArrayList<ChatFormat>();
		for(ChatFormat format : ChatFormat.values()) {
			if(this.json.has(format.toString()) && this.json.get(format.toString()).getAsBoolean()) {
				ret.add(format);
			}
		}
		
		return ret;
	}
	
	public void setFormat(ChatFormat format, boolean active) {
		this.json.addProperty(format.toString(), active);
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
	
	public Message[] getSubMessages() {
		return this.json.has("extra") ? this.arrayToMessages(this.json.get("extra").getAsJsonArray()) : new Message[0];
	}

	public void addSubMessage(Message sub) {
		if(!this.json.has("extra")) {
			this.json.add("extra", new JsonArray());
		}

		JsonArray json = (JsonArray) this.json.get("extra");
		json.add(sub.getJson());
		this.json.add("extra", json);
	}
	
	public String getRawText() {
		StringBuilder build = new StringBuilder();
		String translate = this.getTranslate();
		if(translate != null) {
			build.append(this.getTranslate());
		} else {
			build.append(this.json.get("text").getAsString());
			for(Message msg : this.getSubMessages()) {
				build.append(msg.getRawText());
			}
		}
		
		return build.toString();
	}
	
	public JsonObject getJson() {
		return this.json;
	}

	@Override
	public String toString() {
		return this.json.toString();
	}
	
	private Message[] arrayToMessages(JsonArray array) {
		Message ret[] = new Message[array.size()];
		for(int index = 0; index < array.size(); index++) {
			ret[index] = new Message(array.get(index).isJsonPrimitive() ? array.get(index).getAsString() : array.get(index).toString(), true);
		}
		
		return ret;
	}

}
