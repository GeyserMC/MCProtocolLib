package com.github.steveice10.mc.protocol.data.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class KeybindMessage extends Message {
    private String keybind;

    public KeybindMessage(String keybind) {
        this.keybind = keybind;
    }

    public String getKeybind() {
        return this.keybind;
    }

    @Override
    public String getText() {
        return this.keybind;
    }

    @Override
    public KeybindMessage clone() {
        return (KeybindMessage) new KeybindMessage(this.getKeybind()).setStyle(this.getStyle().clone()).setExtra(this.getExtra());
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

    @Override
    public boolean equals(Object o) {
        return o instanceof KeybindMessage && super.equals(o) && this.keybind.equals(((KeybindMessage) o).keybind);
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 31 + keybind.hashCode();
    }
}
