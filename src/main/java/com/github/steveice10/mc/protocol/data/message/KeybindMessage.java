package com.github.steveice10.mc.protocol.data.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class KeybindMessage extends Message {
    private final String keybind;

    @Override
    public String getText() {
        return this.keybind;
    }

    @Override
    public KeybindMessage clone() {
        return (KeybindMessage) new KeybindMessage(this.keybind).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
    }

    @Override
    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if(e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("keybind", this.keybind);
            return json;
        } else {
            return e;
        }
    }
}
