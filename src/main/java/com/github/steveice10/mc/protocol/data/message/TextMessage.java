package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.Objects;

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
        if(this == o) return true;
        if(!(o instanceof TextMessage)) return false;

        TextMessage that = (TextMessage) o;
        return super.equals(o) &&
                Objects.equals(this.text, that.text);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.text);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
