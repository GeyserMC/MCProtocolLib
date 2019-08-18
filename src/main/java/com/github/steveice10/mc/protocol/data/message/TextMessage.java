package com.github.steveice10.mc.protocol.data.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class TextMessage extends Message {
    private final String text;

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
}
