package ch.spacebase.mc.protocol.data.message;

import java.util.Arrays;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TranslationMessage extends Message {

	private String translationKey;
	private Object translationParams[];
	
	public TranslationMessage(String translationKey, Object... translationParams) {
		this.translationKey = translationKey;
		this.translationParams = translationParams;
		this.translationParams = this.getTranslationParams();
		for(Object param : this.translationParams) {
			if(param instanceof Message) {
				((Message) param).getStyle().setParent(this.getStyle());
			} else if(!(param instanceof String)) {
				throw new IllegalArgumentException("Translation params can only be messages or strings.");
			}
		}
	}
	
	public String getTranslationKey() {
		return this.translationKey;
	}
	
	public Object[] getTranslationParams() {
		Object copy[] = Arrays.copyOf(this.translationParams, this.translationParams.length);
		for(int index = 0; index < copy.length; index++) {
			if(copy[index] instanceof Message) {
				copy[index] = ((Message) copy[index]).clone();
			}
		}
		
		return copy;
	}
	
	@Override
	public Message setStyle(MessageStyle style) {
		super.setStyle(style);
		for(Object param : this.translationParams) {
			if(param instanceof Message) {
				((Message) param).getStyle().setParent(this.getStyle());
			}
		}
		
		return this;
	}
	
	@Override
	public String getText() {
		return this.translationKey;
	}

	@Override
	public TranslationMessage clone() {
		return (TranslationMessage) new TranslationMessage(this.getTranslationKey(), this.getTranslationParams()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
	}
	
	@Override
	public JsonElement toJson() {
		JsonElement e = super.toJson();
		if(e.isJsonObject()) {
			JsonObject json = e.getAsJsonObject();
			json.addProperty("translate", this.translationKey);
			JsonArray params = new JsonArray();
			for(int index = 0; index < this.translationParams.length; index++) {
				Object param = this.translationParams[index];
				if(param instanceof Message) {
					params.add(((Message) param).toJson());
				} else if(param instanceof String) {
					params.add(new JsonPrimitive((String) param));
				}
			}
			
			json.add("with", params);
			return json;
		} else {
			return e;
		}
	}

}
