package org.spacehq.mc.protocol.data.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TextMessage extends Message {

	private String text;

	public TextMessage(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public TextMessage clone() {
		return (TextMessage) new TextMessage(this.getText()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
	}

	@Override
	public JsonElement toJson() {
		if(this.getStyle().isDefault() && this.getExtra().isEmpty()) {
			return new JsonPrimitive(this.text);
		} else {
			JsonElement e = super.toJson();
			if(e.isJsonObject()) {
				JsonObject json = e.getAsJsonObject();
				json.addProperty("text", this.text);
				return json;
			} else {
				return e;
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		TextMessage that = (TextMessage) o;

		if (!text.equals(that.text)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + text.hashCode();
		return result;
	}

}
