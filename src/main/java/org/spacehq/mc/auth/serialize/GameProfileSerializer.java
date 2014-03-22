package org.spacehq.mc.auth.serialize;

import com.google.gson.*;
import org.spacehq.mc.auth.GameProfile;

import java.lang.reflect.Type;
import java.util.UUID;

public class GameProfileSerializer implements JsonSerializer<GameProfile>, JsonDeserializer<GameProfile> {

	@Override
	public GameProfile deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject object = (JsonObject) json;
		UUID id = object.has("id") ? (UUID) context.deserialize(object.get("id"), UUID.class) : null;
		String name = object.has("name") ? object.getAsJsonPrimitive("name").getAsString() : null;
		return new GameProfile(id, name);
	}

	@Override
	public JsonElement serialize(GameProfile src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject result = new JsonObject();
		if(src.getId() != null) {
			result.add("id", context.serialize(src.getId()));
		}

		if(src.getName() != null) {
			result.addProperty("name", src.getName());
		}

		return result;
	}

}
