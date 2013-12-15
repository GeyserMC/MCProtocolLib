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

	public void addExtra(MessageExtra extra) {
		if(!this.json.has("extra")) {
			this.json.add("extra", new JsonArray());
		}

		JsonArray json = (JsonArray) this.json.get("extra");
		json.add(extra.getJson());
		this.json.add("extra", json);
	}
	
	public String getRawText() {
		StringBuilder build = new StringBuilder();
		build.append(this.json.get("text").getAsString());
		if(this.json.has("extra")) {
			JsonArray extra = (JsonArray) this.json.get("extra");
			for(int index = 0; index < extra.size(); index++) {
				build.append(extra.get(index).toString());
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

}
