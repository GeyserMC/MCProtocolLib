package com.github.steveice10.mc.protocol.data.message;

import com.github.steveice10.mc.protocol.util.ObjectUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;

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
        if(this == o) return true;
        if(!(o instanceof KeybindMessage)) return false;

        KeybindMessage that = (KeybindMessage) o;
        return super.equals(o) &&
                Objects.equals(this.keybind, that.keybind);
    }

    @Override
    public int hashCode() {
        return ObjectUtil.hashCode(super.hashCode(), this.keybind);
    }

    @Override
    public String toString() {
        return ObjectUtil.toString(this);
    }
}
